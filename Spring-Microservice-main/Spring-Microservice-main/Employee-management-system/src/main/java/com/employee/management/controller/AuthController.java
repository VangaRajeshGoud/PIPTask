package com.employee.management.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.entity.User;
import com.employee.management.model.UserDetails;
import com.employee.management.model.UserLoginDetails;
import com.employee.management.service.JwtHelper;
import com.employee.management.service.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")

@RequestMapping("/employees/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody UserDetails requestDto, BindingResult result) {
		if (result.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			result.getAllErrors()
					.forEach(error -> errors.append(error.getDefaultMessage()).append(System.lineSeparator()));
			return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
		}
		if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
			return new ResponseEntity<>("Passwords do not match!", HttpStatus.BAD_REQUEST);
		}
		User newUser = new User();
		newUser.setFirstName(requestDto.getFirstName());
		newUser.setLastName(requestDto.getLastName());
		newUser.setUsername(requestDto.getUsername());
		newUser.setEmail(requestDto.getEmail());
		newUser.setPassword(requestDto.getPassword());
		userService.signup(newUser);

		return new ResponseEntity<>("User successfully created!", HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDetails request, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		if (result.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append(System.lineSeparator()));
			response.put("message", "Validation failed");
			response.put("errors", errors.toString());
			response.put("time", currentTime);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					request.getUsername(), request.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = JwtHelper.generateToken(request.getUsername());
			response.put("message", "Authentication successful");
			response.put("username", request.getUsername());
			response.put("token", token);
			response.put("refreshToken", token);
			response.put("time", currentTime);
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			response.put("message", "Invalid username or password");
			response.put("time", currentTime);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		} catch (Exception e) {
			response.put("message", e.getMessage());
			response.put("time", currentTime);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
