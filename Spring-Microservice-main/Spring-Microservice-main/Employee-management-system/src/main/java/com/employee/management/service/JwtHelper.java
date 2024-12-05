package com.employee.management.service;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {

	public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final int MINUTES = 60;
	private static final long EXPIRATION_TIME_MINUTES = 60;

	public static String generateToken(String userName) {
		var now = Instant.now();
		return Jwts.builder().subject(userName).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public static String extractUsername(String token) throws AccessDeniedException {
		return getTokenBody(token).getSubject();
	}

	public static Boolean validateToken(String token, UserDetails userDetails) throws AccessDeniedException {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	static Claims getTokenBody(String token) throws AccessDeniedException {
		try {
			return Jwts.parser().setSigningKey(SECRET_KEY).build().parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			throw new AccessDeniedException("Access denied: " + e.getMessage());
		}
	}

	public static boolean isTokenExpired(String token) throws AccessDeniedException {
		Claims claims = getTokenBody(token);
		return claims.getExpiration().before(new Date());
	}
}