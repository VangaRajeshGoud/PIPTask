package com.employee.management.config;

import com.employee.management.service.JwtHelper;
import com.employee.management.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private ObjectMapper objectMapper;

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			System.out.println("JwtAuthFilter ");
			String authHeader = request.getHeader("Authorization");
			String token = null;
			String username = null;

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				username = JwtHelper.extractUsername(token);
			}
			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (JwtHelper.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, null);
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}

			filterChain.doFilter(request, response);

		} catch (AccessDeniedException e) {
			RuntimeException errorResponse = new RuntimeException(
					HttpServletResponse.SC_FORBIDDEN + " : " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write(toJson(errorResponse));
		}
	}

	private String toJson(RuntimeException response) {
		try {
			return objectMapper.writeValueAsString(response);
		} catch (Exception e) {
			return "";
		}
	}

}