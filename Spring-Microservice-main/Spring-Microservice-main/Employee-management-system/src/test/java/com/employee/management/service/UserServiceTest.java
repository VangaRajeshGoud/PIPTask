package com.employee.management.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.employee.management.entity.User;
import com.employee.management.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSignup_UserDoesNotExist() {
		User newUser = createDummyUser();
		userService.signup(newUser);
	}

	public static User createDummyUser() {
		User user = new User();
		user.setFirstName("Test");
		user.setLastName("Test2");
		user.setUsername("Test3");
		user.setEmail("john.doe@example.com");
		user.setPassword("password123");
		return user;
	}
}
