import psycopg2
from psycopg2 import sql
import requests
import json
import argparse


class Region:
    def __init__(self, name, bounds):
        self.name = name
        self.bounds = bounds


class NPSRegion(Region):
    """
    namekey refers to the key used to find the trail name in the particular
    data source this region refers to, since each has a different one
    """
    def __init__(self, name, bounds, name_key, dataset_id):
        super().__init__(name, bounds)
        self.name_key = name_key
        self.dataset_id = dataset_id

    def getAPIUrl(self):
        return "https://opendata.arcgis.com/datasets/" + self.dataset_id + ".geojson"


def load_regions(fp):
    regions_json = json.load(open(fp))
    regions = []
    for r in regions_json:
        if r["type"] == "nps":
            regions.append(NPSRegion(
                r["name"], r["bounds"], r["name_key"], r["dataset_id"]
            ))
    return regions

def get_geojson(region):
    r = requests.get(region.getAPIUrl())
    return r.json()

# some datasources have multilinestrings as the data type. the goal is to decompose
# them into several linestrings so those can be added as segments
def decompose_multi_line_string(geom):
    linestrings = []
    for line in geom["coordinates"]:
        ls = {"type": "LineString", "coordinates": line}
        linestrings.append(json.dumps(ls))
    return linestrings

# returns a dictionary, where the key is the trail name and the value is a list of all of
# the geometry segments (in the form of a GeoJSON string)
def extract_trails(region, geojson):
    trails = {}
    for f in geojson["features"]:
        name = f["properties"][region.name_key]
        geom = f["geometry"]
        if not name in trails:
            trails[name] = []

        if geom["type"] == "MultiLineString":
            trails[name].extend(decompose_multi_line_string(geom))
        else:
            trails[name].append(json.dumps(geom))
    return trails

def create_database(args):
    conn = psycopg2.connect("user={} password={}".format(args.user, args.passwd))
    conn.autocommit = True
    cur = conn.cursor()
    cur.execute("DROP DATABASE IF EXISTS nmtrails")
    cur.execute("CREATE DATABASE nmtrails")
    cur.close()
    conn.close()

def add_trails_to_db(trails, cursor, regionid):
    for trail in trails:
        query = """INSERT INTO trails (avg_rating, name, ratings, region) 
                   VALUES (0, %s, 0, %s) RETURNING id"""
        cursor.execute(query, [trail, regionid])

        trail_id = cursor.fetchone()[0]
        for segment in trails[trail]:
            #print(segment)
            query = """INSERT INTO segments (trail, track) VALUES
                     (%s, ST_Force2D(ST_GeomFromGeoJSON(%s)))"""
            cursor.execute(query, [trail_id, segment])

def add_region_to_db(region, cursor):
    query = "INSERT INTO regions (name) VALUES (%s) RETURNING id"
    cursor.execute(query, [region.name])
    return cursor.fetchone()[0]

def parse_args():
    parser = argparse.ArgumentParser(description="Initialize the database.")
    parser.add_argument('user', type=str, help='Postgres user name')
    parser.add_argument('passwd', type=str, help='Postgres password')
    return parser.parse_args()

if __name__ == "__main__":
    args = parse_args()

    create_database(args)
    conn = psycopg2.connect("dbname=nmtrails user={} password={}".format(args.user, args.passwd))
    conn.autocommit=True
    cur = conn.cursor()
    cur.execute(open("init.sql", "r").read())

    regions = load_regions("regions.json")
    for region in regions:
        print("Processing {}...".format(region.name))
        rid = add_region_to_db(region, cur)
        geojson = get_geojson(region)
        trails = extract_trails(region, geojson)
        add_trails_to_db(trails, cur, rid)
