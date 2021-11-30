package com.nmtrails.appcontest;

import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.repositories.TrailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TrailsRESTTests {

    @Autowired
    private TrailRepository repo;

    @Autowired
    private MockMvc mockMvc;

    @BeforeTestClass
    void saveTrails() {
        for (int i = 0; i < 20; i++) {
            Trail t = new Trail();
            t.setName(String.format("Trail%d", i));
            repo.save(t);
        }
    }

    @Test
    void testPagination() {

    }
}
