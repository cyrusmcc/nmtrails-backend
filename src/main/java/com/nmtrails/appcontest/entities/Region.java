package com.nmtrails.appcontest.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    Set<Trail> trails = new HashSet<>();

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Set<Trail> getTrails() {
        return trails;
    }
}
