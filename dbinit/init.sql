CREATE EXTENSION postgis;

CREATE TABLE trails (
    trail_id bigserial PRIMARY KEY,
    sum_of_ratings int4,
    avg_rating float8,
    name varchar(255),
    region_region_id int8,
    ratings int4,
    has_image boolean DEFAULT false,
	image_url varchar(1000)
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
