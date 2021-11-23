package com.nmtrails.appcontest;

import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppContestApplicationTests {

	@Autowired
	private UserRepository userRepository;


	@Test
	void contextLoads() {
	}

	@Test
	void addUser() {

		User user = new User();

		user.setUsername("test");

		userRepository.save(user);

	}

}
