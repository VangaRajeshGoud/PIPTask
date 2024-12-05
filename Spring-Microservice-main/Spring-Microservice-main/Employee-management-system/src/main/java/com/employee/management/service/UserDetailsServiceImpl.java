package com.employee.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.employee.management.entity.User;
import com.employee.management.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		User user = repository.findByUsername(userName)
				.orElseThrow(() -> new RuntimeException(String.format("User does not exist, userName: %s", userName)));

		return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
				.password(user.getPassword()).authorities(authorities).build();
	}

}