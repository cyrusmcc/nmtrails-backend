CREATE EXTENSION postgis;

CREATE TABLE users (
    user_id serial PRIMARY KEY,
    username varchar(255)
);

CREATE TABLE trails (
    trail_id serial PRIMARY KEY,
    avg_rating real,
    name varchar,
    region_id int,
    ratings int
);

CREATE TABLE segments (
    segment_id serial PRIMARY KEY,
    trail_id int,
    track geometry(LineString)
);

CREATE TABLE trailheads (
    trailhead_id serial PRIMARY KEY,
    trail int,
    point geometry(Point)
);

CREATE TABLE regions (
    region_id serial PRIMARY KEY,
    name varchar
);