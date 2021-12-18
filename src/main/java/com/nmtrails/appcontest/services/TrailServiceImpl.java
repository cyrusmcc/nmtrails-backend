package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public List<Trail> findAllByNameLike(String name, Pageable pageable) {
        String nameLike = String.format("%%%s%%", name);
        return repo.findAllByNameLike(nameLike, pageable).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

}
