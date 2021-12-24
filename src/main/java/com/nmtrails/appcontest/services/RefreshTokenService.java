package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    int deleteByUserId(Long userId);


}
