CREATE EXTENSION postgis;

CREATE TABLE trails (
    trail_id bigserial PRIMARY KEY,
    avg_rating float4,
    name varchar(255),
    region_region_id int8,
    ratings int4,
    has_image boolean DEFAULT false
);

CREATE TABLE segments (
    segment_id bigserial PRIMARY KEY,
    trail_trail_id int8,
    track geometry(LineString)
);

CREATE TABLE trailheads (
    id serial PRIMARY KEY,
    trail int,
    point geometry(Point)
);

CREATE TABLE regions (
    region_id serial PRIMARY KEY,
    name varchar
);