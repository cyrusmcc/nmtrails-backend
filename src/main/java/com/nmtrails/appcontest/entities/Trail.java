package com.nmtrails.appcontest.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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
    @JsonIgnore
    private int sumOfRatings = 0;

    @Column
    @JsonIgnore
    private int ratings = 0;

    @Column
    private float avgRating = 0;

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

    public int getSumOfRatings() {
        return sumOfRatings;
    }

    public void setSumOfRatings(int sumOfRatings) {
        this.sumOfRatings = sumOfRatings;
    }

    public int getRatings() {
        return ratings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public float setAvgRating() {
        if (ratings > 0)
            avgRating = (sumOfRatings * 5) / (ratings * 5);

        return 0;
    }

    public float getAvgRating() {
        return this.avgRating;
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
