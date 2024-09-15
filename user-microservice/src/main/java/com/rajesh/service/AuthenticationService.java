package com.rajesh.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajesh.dto.LoginDTO;
import com.rajesh.dto.RegisterDTO;
import com.rajesh.entity.User;
import com.rajesh.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User signup(RegisterDTO input) {
		if (input == null || input.getUsername() == null || input.getPassword() == null) {
			throw new IllegalArgumentException("Invalid input");
		}

		var user = new User();
		user.setUsername(input.getUsername());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setRole(input.getRole());

		try {
			return userRepository.save(user);
		} catch (Exception e) {
			// Log the exception and rethrow it or handle it accordingly
			e.printStackTrace();
			throw new RuntimeException("Error saving user", e);
		}
	}

	public User authenticate(LoginDTO input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

		return userRepository.findByUsername(input.getUsername()).orElseThrow();
	}

	public List<User> allUsers() {
		List<User> users = new ArrayList<>();

		userRepository.findAll().forEach(users::add);

		return users;
	}
}
