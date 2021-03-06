package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.User;

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

    void updatePassword(User user, String password);

    boolean isValidPassword(User user, String password);

}
