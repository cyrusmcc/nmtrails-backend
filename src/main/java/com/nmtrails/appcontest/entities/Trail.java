package com.nmtrails.appcontest.entities;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;

@Entity
@Table(name="trails")
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LineString track;

    @Column
    private String name;

    @Column
    private float avgRating = 0;

    @Column
    private int ratings = 0;

    @Column
    private Point trailhead;

    public Long getId() {
        return id;
    }

    public LineString getTrack() {
        return track;
    }

    public String getName() {
        return name;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public int getRatings() {
        return ratings;
    }

    public Point getTrailhead() {
        return trailhead;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
}