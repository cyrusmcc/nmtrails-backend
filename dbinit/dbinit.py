import psycopg2
from psycopg2 import sql
import requests
import json
import argparse

USFS_API_FORMAT_STR = open("usfs.txt").read()

class Region:
    def __init__(self, name, name_key):
        self.name = name
        self.name_key = name_key


class NPSRegion(Region):
    def __init__(self, name, dataset_id):
        super().__init__(name, "TRLNAME")
        self.dataset_id = dataset_id

    def getAPIUrl(self):
        return "https://opendata.arcgis.com/datasets/" + self.dataset_id + ".geojson"


class USFSRegion(Region):

    def __init__(self, name, bounds):
        super().__init__(name, "TRAIL_NAME")
        self.bounds = bounds

    def getAPIUrl(self):
        bounds = self.bounds
        return USFS_API_FORMAT_STR.format(
            bounds[0][0], bounds[0][1], bounds[1][0], bounds[1][1]
        )


def load_regions(fp):
    regions_json = json.load(open(fp))
    regions = []
    for r in regions_json:
        if r["type"] == "nps":
            regions.append(NPSRegion(
                r["name"], r["dataset_id"]
            ))
        elif r["type"] == "usfs":
            regions.append(USFSRegion(
                r["name"], r["bounds"]
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

def process_geometry(geom, trails, name):
    if geom["type"] == "MultiLineString":
        trails[name].extend(decompose_multi_line_string(geom))
    else:
        trails[name].append(json.dumps(geom))

# returns a dictionary, where the key is the trail name and the value is a list of all of
# the geometry segments (in the form of a GeoJSON string)
def extract_trails(region, geojson):
    trails = {}
    for f in geojson["features"]:
        name = f["properties"][region.name_key]
        if name is None:
            name = ""
        name = name.title()
        if "Trail" not in name:
            name = name + " Trail"
        geom = f["geometry"]
        if not name in trails:
            trails[name] = []
        process_geometry(geom, trails, name)
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
        query = """INSERT INTO trails (sum_of_ratings, name, ratings, avg_rating, region_region_id)
                   VALUES (0, %s, 0, 0, %s) RETURNING trail_id"""
        cursor.execute(query, [trail, regionid])

        trail_id = cursor.fetchone()[0]
        for segment in trails[trail]:
            #print(segment)
            query = """INSERT INTO segments (trail_trail_id, track) VALUES
                     (%s, ST_Force2D(ST_GeomFromGeoJSON(%s)))"""
            cursor.execute(query, [trail_id, segment])

def add_region_to_db(region, cursor):
    query = "INSERT INTO regions (name) VALUES (%s) RETURNING region_id"
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

    print("Finished initializing database.")

