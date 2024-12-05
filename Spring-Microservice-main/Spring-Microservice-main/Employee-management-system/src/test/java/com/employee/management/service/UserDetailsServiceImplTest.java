package com.employee.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.employee.management.entity.User;
import com.employee.management.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private User mockUser;

	@BeforeEach
	void setUp() {
		mockUser = new User();
		mockUser.setUsername("testUser");
		mockUser.setPassword("testPassword");
	}

	@Test
    void testLoadUserByUsername_ValidUser() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());  
    }

	@Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            userDetailsService.loadUserByUsername("nonExistentUser");
        });
    }
}