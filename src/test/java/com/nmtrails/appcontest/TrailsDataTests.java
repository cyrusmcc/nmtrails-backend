package com.nmtrails.appcontest;

import com.nmtrails.appcontest.controllers.TrailController;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import com.nmtrails.appcontest.services.TrailService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Transactional
public class TrailsDataTests {

    @Autowired
    private TrailRepository repo;
    @Autowired
    private TrailService trails;

    @BeforeEach
    void saveTrails() {
        Trail t1 = new Trail();
        t1.setName("trail");
        Trail t2 = new Trail();
        t2.setName("no");

        repo.save(t1);
        repo.save(t2);
    }

    @Test
    void contextLoads() {
        assertNotNull(repo);
        assertNotNull(trails);
    }

    @Test
    void testFindByNameLike() {
        PageRequest pr = PageRequest.of(0, 100);
        List<Trail> result = trails.findAllByNameLike("ail", pr);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().equals("trail"));
    }
}
