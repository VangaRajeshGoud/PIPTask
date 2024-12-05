package com.employee.management.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employee.management.entity.User;
import com.employee.management.exception.DuplicateException;
import com.employee.management.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void signup(User request) {
		String userName = request.getUsername();
		Optional<User> existingUser = repository.findByUsername(userName);
		if (existingUser.isPresent()) {
			throw new DuplicateException(String.format("userName '%s' already exists.", userName));
		}
		String hashedPassword = passwordEncoder.encode(request.getPassword());
		request.setPassword(hashedPassword);
		repository.save(request);
	}

}