package com.nmtrails.appcontest.entityTests;

import com.nmtrails.appcontest.controllers.UserController;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.repositories.TrailRepository;
import com.nmtrails.appcontest.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    UserService userService;

    @Autowired
    TrailRepository trailRepository;

    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
    }

    @Test
    User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("test");
        user.setEmail("testemail");
        userService.save(user);

        return user;
    }

    @Test
    void getUserByUsername() {

        User user = createUser();
        assert (userService.findByUsername("testuser").equals(user));

    }

    @Test
    void userHasWishListTrails() {

        User user = createUser();
        Set<Trail> wL = user.getWishList();

        Trail t1 = new Trail();
        t1.setName("t1");
        trailRepository.save(t1);
        wL.add(t1);

        Trail t2 = new Trail();
        t2.setName("t2");
        trailRepository.save(t2);
        wL.add(t2);

        userService.save(user);

        for (Trail t : wL) System.out.println(t.getName());

        assert (user.getWishList().size() == 2);
        assert (user.getWishList().contains(t1) && user.getWishList().contains(t2));

    }

    @Test
    void userDoesNotHaveTrailInWishList() {

        User user = createUser();
        Set<Trail> wL = user.getWishList();

        Trail trail = new Trail();
        trail.setName("testTrail");
        trailRepository.save(trail);

        assert (!wL.contains(trail));

    }

    @Test
    void userHasTrailInWishList() {

        User user = createUser();

        Trail trail = new Trail();
        trail.setName("testTrail");
        trailRepository.save(trail);
        user.addTrailToWishList(trail);
        userService.save(user);

        assert (user.hasTrailInWishList(trail));

        Assertions.assertThrows (IllegalArgumentException.class, () -> {
                user.addTrailToWishList(trail);
        });

    }

}
