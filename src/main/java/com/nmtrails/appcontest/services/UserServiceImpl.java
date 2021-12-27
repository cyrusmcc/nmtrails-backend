package com.nmtrails.appcontest.services;

import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new RuntimeException("User " + id + " not found");

        }

        return user.get();
    }

    @Override
    public User findByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new RuntimeException("User with email " + email + " Not found.");
        }

        return user.get();
    }

    @Override
    public User findByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new RuntimeException("User " + username + " Not found.");
        }

        return user.get();
    }

    @Override
    public boolean existsById(Long id) {
        if (userRepository.existsById(id)) return true;
        else return false;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (userRepository.existsByUsername(username)) return true;
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) return true;
        return false;
    }

    @Override
    public void newUser(User user) {

        user.setId(0L);
        user.setUserJoinDate(LocalDate.now());
        save(user);

    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updatePassword(User user, String password) {

        String encodedPass = passwordEncoder.encode(password);
        user.setPassword(encodedPass);
        userRepository.save(user);

    }

    @Override
    public boolean isValidPassword(User user, String password) {

        if (passwordEncoder.matches(password, user.getPassword()))
            return true;

        return false;
    }

}
