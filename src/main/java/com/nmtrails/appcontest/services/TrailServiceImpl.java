package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class TrailServiceImpl implements TrailService {

    TrailRepository repo;

    @Autowired
    public TrailServiceImpl(TrailRepository repo) {
        this.repo = repo;
    }

    @Override
    public Trail findById(Long id) {
        return repo.getById(id);
    }

    @Override
    public Trail findByNameLike(String name) {
        return repo.findByNameLike(name).orElseThrow();
    }
}
