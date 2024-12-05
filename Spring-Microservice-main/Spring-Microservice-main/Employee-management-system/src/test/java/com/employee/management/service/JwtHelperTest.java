package com.employee.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;

class JwtHelperTest {

	private static final String USERNAME = "testUser";
	private static final String SECRET_KEY = "mysecretkeyforjwtsigning";
	private JwtHelper jwtHelper;

	@BeforeEach
	void setUp() {
		jwtHelper = new JwtHelper();
	}

	@Test
	void testGenerateToken() {
		String token = JwtHelper.generateToken(USERNAME);
		assertNotNull(token);
	}

	@Test
	void testExtractUsername() throws AccessDeniedException {
		String token = JwtHelper.generateToken(USERNAME);
		String extractedUsername = JwtHelper.extractUsername(token);
		assertEquals(USERNAME, extractedUsername);
	}

	@Test
	void testValidateTokenValid() throws AccessDeniedException {
		UserDetails userDetails = User.builder().username(USERNAME).password("password")
				.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) // Add authority here
				.build();
		String token = JwtHelper.generateToken(USERNAME);

		Boolean isValid = JwtHelper.validateToken(token, userDetails);

		assertTrue(isValid);
	}

	@Test
	void testValidateTokenInvalid() throws AccessDeniedException {
		UserDetails userDetails = User.builder().username("differentUser").password("password")
				.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) // Add authority here
				.build();
		String token = JwtHelper.generateToken(USERNAME);

		Boolean isValid = JwtHelper.validateToken(token, userDetails);

		assertFalse(isValid);
	}

	@Test
	void testIsTokenExpired() {
		long expiredTime = Instant.now().minusSeconds(3601).getEpochSecond();
		String expiredToken = Jwts.builder().setSubject(USERNAME).setExpiration(new Date(expiredTime * 1000))
				.signWith(JwtHelper.SECRET_KEY).compact();
		assertThrows(AccessDeniedException.class, () -> JwtHelper.isTokenExpired(expiredToken),
				"AccessDeniedException should be thrown for expired token");
	}

	@Test
	void testIsTokenNotExpired() throws AccessDeniedException {
		String token = JwtHelper.generateToken(USERNAME);
		assertFalse(JwtHelper.isTokenExpired(token));
	}

	@Test
	void testExtractUsernameWhenExpired() {
		long expiredTime = Instant.now().minusSeconds(3600).getEpochSecond();
		String expiredToken = Jwts.builder().setSubject(USERNAME).setExpiration(new Date(expiredTime * 1000))
				.signWith(JwtHelper.SECRET_KEY).compact();
		assertThrows(AccessDeniedException.class, () -> JwtHelper.extractUsername(expiredToken));
	}

	@Test
	void testGetTokenBodyWithExpiredToken() {
		long expiredTime = Instant.now().minusSeconds(3600).getEpochSecond();
		String expiredToken = Jwts.builder().setSubject(USERNAME).setExpiration(new Date(expiredTime * 1000))
				.signWith(JwtHelper.SECRET_KEY).compact();
		assertThrows(AccessDeniedException.class, () -> JwtHelper.getTokenBody(expiredToken));
	}

}
