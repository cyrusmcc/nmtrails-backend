package com.nmtrails.appcontest.entities;

import org.locationtech.jts.geom.LineString;

import javax.persistence.*;

@Entity
@Table(name="segments")
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "segment_id")
    private Long id;

    @ManyToOne
    private Trail trail;

    @Column
    private LineString track;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LineString getTrack() {
        return track;
    }
}
