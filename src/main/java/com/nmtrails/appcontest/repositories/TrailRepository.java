package com.nmtrails.appcontest.repositories;

import com.nmtrails.appcontest.entities.Trail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {

    Optional<Trail> findById(Long id);

    Page findAllByNameLike(String nameLike, Pageable pageable);
}
