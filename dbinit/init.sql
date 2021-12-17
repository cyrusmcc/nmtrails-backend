CREATE EXTENSION postgis;

CREATE TABLE users (
    user_id serial PRIMARY KEY,
    username varchar(255)
);

CREATE TABLE trails (
    id serial PRIMARY KEY,
    avg_rating real,
    name varchar,
    region int,
    ratings int
);

CREATE TABLE segments (
    id serial PRIMARY KEY,
    trail int,
    track geometry(LineString)
);

CREATE TABLE trailheads (
    id serial PRIMARY KEY,
    trail int,
    point geometry(Point)
);

CREATE TABLE regions (
    id serial PRIMARY KEY,
    name varchar
);