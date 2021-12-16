package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.entities.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void newUser(User user);

    void save(User user);

    void deleteById(Long id);

}
