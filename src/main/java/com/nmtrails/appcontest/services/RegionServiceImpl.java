package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Region;
import com.nmtrails.appcontest.entities.RegionView;
import com.nmtrails.appcontest.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    @Override
    public RegionView randomRegion() {
        long records = repo.countAllBy();

        long page = ThreadLocalRandom.current().nextLong(records);
        PageRequest pr = PageRequest.of((int) page, 1);
        return repo.findAllBy(pr).toList().get(0);
    }
}
