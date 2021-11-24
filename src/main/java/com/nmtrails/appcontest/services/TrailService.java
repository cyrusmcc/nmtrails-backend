package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;

import java.util.Optional;

public interface TrailService {

    Trail findById(Long id);
    Trail findByNameLike(String name);
}
