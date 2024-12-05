package com.employee.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.employee.management.entity.User;
import com.employee.management.model.UserDetails;
import com.employee.management.repository.UserRepository;
import com.employee.management.service.JwtHelper;
import com.employee.management.service.UserService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserService userService;

	@Mock
	private JwtHelper jwtHelper;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AuthController authController;

	private UserDetails userDetails;
	private UserDetails invalidUserDetails;

	@BeforeEach
	void setUp() {
		userDetails = new UserDetails("John", "Doe", "johndoe", "john@example.com", "password", "password");
		invalidUserDetails = new UserDetails("John", "Doe", "johndoe", "john@example.com", "password", "wrongpassword");
	}

	@Test
	void testLoginSuccess() throws Exception {
		User user = new User();
		user.setUsername("johndoe");
		UserDetails request = new UserDetails("johndoe", "password");
		when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		String generatedToken = "dummy-jwt-token";
		String requestBody = "{ \"username\": \"johndoe\", \"password\": \"password\" }";
		mockMvc.perform(post("/employees/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Authentication successful"))
				.andExpect(jsonPath("$.username").value("johndoe")).andExpect(jsonPath("$.token").value(generatedToken))
				.andExpect(jsonPath("$.refreshToken").value(generatedToken));
	}

	@Test
	void testSignupSuccess() throws Exception {
		String requestBody = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"username\": \"johndoe\", \"email\": \"john@example.com\", \"password\": \"password\", \"confirmPassword\": \"password\" }";
		mockMvc.perform(post("/employees/auth/signup").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated()).andExpect(content().string("User successfully created!"));
	}

	@Test
	void testSignupPasswordMismatch() throws Exception {
		String requestBody = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"username\": \"johndoe\", \"email\": \"john@example.com\", \"password\": \"password\", \"confirmPassword\": \"differentpassword\" }";
		mockMvc.perform(post("/employees/auth/signup").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest()).andExpect(content().string("Passwords do not match!"));
	}

	@Test
	void testSignupMissingFields() throws Exception {
		String requestBody = "{ \"firstName\": \"\", \"lastName\": \"\", \"username\": \"\", \"email\": \"\", \"password\": \"\", \"confirmPassword\": \"\" }";
		mockMvc.perform(post("/employees/auth/signup").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isBadRequest());
	}

}
