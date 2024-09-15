package com.rajesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajesh.dto.LoginDTO;
import com.rajesh.dto.RegisterDTO;
import com.rajesh.entity.User;
import com.rajesh.responses.LoginResponse;
import com.rajesh.service.AuthenticationService;

import com.rajesh.service.JwtService;
import com.rajesh.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/users")
@RestController
public class UserController {
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	public UserController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterDTO registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
				.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

	/*
	 * @PostMapping("/logout") public ResponseEntity<?>
	 * logout(@RequestHeader("Authorization") String token) { if (token != null &&
	 * token.startsWith("Bearer ")) { token = token.substring(7);
	 * jwtBlacklistService.blacklistToken(token); } return
	 * ResponseEntity.ok("Logged out successfully"); }
	 */

	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
		// Extract username from the token
		String tokenWithoutBearer = token.substring(7);
		String username = jwtService.extractUsername(tokenWithoutBearer);

		// Fetch UserDetails using the username
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		// Pass the token and UserDetails to the JwtService for validation
		Boolean isValid = jwtService.isTokenValid(tokenWithoutBearer, userDetails);

		return ResponseEntity.ok(isValid);
	}

	@GetMapping("/roles")
	public ResponseEntity<String> getUserRoles(@RequestHeader("Authorization") String token) {
		String username = jwtService.extractUsername(token.substring(7));
		User user = userService.getUserByUsername(username);
		return ResponseEntity.ok(user.getRole());
	}
}
