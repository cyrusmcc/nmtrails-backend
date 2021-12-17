package com.nmtrails.appcontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nmtrails.appcontest.entities.Trail;
import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.repositories.TrailRepository;
import com.nmtrails.appcontest.repositories.UserRepository;
import com.nmtrails.appcontest.services.UserService;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
class AppContestApplicationTests {

	@Test
	void contextLoads() {
	}
}
