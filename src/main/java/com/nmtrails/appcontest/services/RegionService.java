package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegionService {

    Region findById(Long id);
    List<RegionView> findAll(Pageable pageable);
}
