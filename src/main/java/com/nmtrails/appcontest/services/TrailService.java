package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrailService {

    Trail findById(Long id);
    Trail findByNameLike(String name);
    List<Trail> findBy(Pageable pageable);
}
