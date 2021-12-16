package com.nmtrails.appcontest.entities;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;

@Entity
@Table(name="trails")
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trail_id")
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

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
    private Set<User> wishListedUsers = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
    private Set<User> hikedListUsers = new HashSet<>();
    */

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

    /*
    public Set<User> getWishListedUsers() {
        return wishListedUsers;
    }

    public void setWishListedUsers(Set<User> wishListedUsers) {
        this.wishListedUsers = wishListedUsers;
    }

    public Set<User> getHikedListUsers() {
        return hikedListUsers;
    }

    public void setHikedListUsers(Set<User> hikedListUsers) {
        this.hikedListUsers = hikedListUsers;
    }
    */
}
