package com.nmtrails.appcontest.entities;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="trails")
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trail_id")
    private Long id;

    @Column
    private String name;

    @Column
    private float avgRating = 0;

    @Column
    private int ratings = 0;

    @Column(length = 1000)
    private String imageUrl;

    @Column
    private boolean hasImage = false;

    @OneToMany(mappedBy = "trail", fetch = FetchType.LAZY)
    private Set<Segment> segments = new HashSet<>();

    @ManyToOne
    private Region region;

    public Long getId() {
        return id;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public String getImageUrl() {return this.imageUrl;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }
}
