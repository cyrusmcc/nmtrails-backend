package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrailService {

    Trail findById(Long id);

    List<Trail> findAllByNameLike(String name, Pageable pageable);

    boolean existsById(Long id);

    Geometry findExtent(List<Long> ids);

    void save(Trail trail);

    List<Trail> findAllByRatingsDesc(Integer pageNum, Integer pageSize);

    List<Trail> getRandomTrails(int numRandom);
}
