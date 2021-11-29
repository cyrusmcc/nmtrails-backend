package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailServiceImpl implements TrailService {

    TrailRepository repo;

    @Autowired
    public TrailServiceImpl(TrailRepository repo) {
        this.repo = repo;
    }

    @Override
    public Trail findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Trail findByNameLike(String name) {
        return repo.findByNameLike(name).orElseThrow();
    }

    @Override
    public List<Trail> findAll(Pageable pageable) {
        return repo.findAll(pageable).toList();
    }

}
