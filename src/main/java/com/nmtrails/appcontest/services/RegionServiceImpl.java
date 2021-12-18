package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import com.nmtrails.appcontest.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    RegionRepository repo;

    @Autowired
    public RegionServiceImpl(RegionRepository repo) {
        this.repo = repo;
    }

    @Override
    public Region findById(Long id) {
        Optional<Region> region = repo.findById(id);
        if (region.isPresent()) return region.get();
        else throw new RuntimeException("Region not found.");
    }

    @Override
    public List<RegionView> findAll(Pageable pageable) {
        return repo.findAllBy(pageable).toList();
    }
}
