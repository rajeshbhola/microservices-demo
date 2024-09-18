package com.rajesh.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> register(@RequestBody RegisterDTO registerUserDto) {
	    // Use findByUsername or findByEmail to check if a user already exists
	    Optional<User> existingUser = authenticationService.findByUsername(registerUserDto.getUsername());
	    
	    if (existingUser.isPresent()) {
	        // Return a 409 Conflict response if the user already exists
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("User with the same username already exists.");
	    }

	    // Proceed with registration if the user doesn't exist
	    User registeredUser = authenticationService.signup(registerUserDto);

	    return ResponseEntity.ok(registeredUser);
	}


	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginUserDto) {
	    // Authenticate the user (returns a custom User object)
	    User authenticatedUser = authenticationService.authenticate(loginUserDto);

	    // Assuming authenticatedUser can be cast or is equivalent to UserDetails
	    UserDetails userDetails = (UserDetails) authenticatedUser;

	    // Extract userId from authenticatedUser (assuming it has a getId() method)
	    Long userId = authenticatedUser.getId();

	    // Generate JWT token with userId and other details
	    String jwtToken = jwtService.generateToken(new HashMap<>(), userDetails, userId);

	    // Create login response with token and expiration time
	    LoginResponse loginResponse = new LoginResponse()
	            .setToken(jwtToken)
	            .setExpiresIn(jwtService.getExpirationTime());

	    return ResponseEntity.ok(loginResponse);
	}




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
	
	// Endpoint to check if a user exists by their ID
    @GetMapping("/{id}")
    public ResponseEntity<Boolean> isUserExists(@PathVariable("id") Long id) {
        Boolean userExists = userService.isUserExists(id);
        return ResponseEntity.ok(userExists);
    }
	
	@GetMapping("/userid")
	public ResponseEntity<Long> getUserId(@RequestHeader("Authorization") String token) {
		long userid = jwtService.extractUserId(token.substring(7));
		return ResponseEntity.ok(userid);
	}
	
}
