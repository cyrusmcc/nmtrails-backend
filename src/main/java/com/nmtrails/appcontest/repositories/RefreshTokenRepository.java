package com.nmtrails.appcontest.repositories;

import com.nmtrails.appcontest.entities.RefreshToken;
import com.nmtrails.appcontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);
}