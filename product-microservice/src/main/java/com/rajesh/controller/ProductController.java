package com.rajesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rajesh.dto.ProductDTO;
import com.rajesh.entity.Product;
import com.rajesh.feign.UserServiceFeignClient;
import com.rajesh.service.ProductService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserServiceFeignClient userClient;

	@Value("${role.seller}")
	private String role_seller;

	@Value("${role.customer}")
	private String role_customer;

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addProduct(@RequestHeader("Authorization") String token,
	        @RequestBody ProductDTO productDto) {
	    // Validate the JWT token
	    Boolean isValid = userClient.validateToken(token);
	    if (isValid) {
	        // Get user roles from the token
	        String roles = userClient.getUserRoles(token);
	        if (roles.contains(role_seller)) {
	            // Extract user ID from the token
	            Long userId = userClient.getUserId(token); // You need to implement getUserId in UserClient

	            // Create and save the product
	            Product createdProduct = productService.createProduct(productDto, userId);

	            // Create a response map
	            Map<String, String> response = new HashMap<>();
	            response.put("message", "Product added successfully with ID: " + createdProduct.getId());
	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body(Collections.singletonMap("error", "You are not authorized to add products"));
	        }
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(Collections.singletonMap("error", "Invalid Token"));
	}


	@GetMapping("/view")
	public ResponseEntity<List<Product>> viewProducts(@RequestHeader("Authorization") String token) {
		Boolean isValid = userClient.validateToken(token);
		if (isValid) {
			String roles = userClient.getUserRoles(token);
			if (roles.contains(role_customer) || roles.contains(role_seller)) {
				return ResponseEntity.ok(productService.getAllProducts());
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<Product>> getProductsByUserId(@RequestHeader("Authorization") String token,
	                                                          @PathVariable("id") Long id) {
	    // Step 1: Validate the JWT Token
	    Boolean isValid = userClient.validateToken(token);

	    if (isValid) {
	        // Step 2: Extract User ID from the token
	        Long userId = userClient.getUserId(token);

	        // Step 3: Call user service to verify if the user exists
	        if (userId != null && userClient.isUserExists(token, userId)) {
	            // Step 4: Retrieve the products linked to this user
	            List<Product> products = productService.getProducts(userId);

	            if (products.isEmpty()) {
	                // Return NOT FOUND if no products exist for the user
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }

	            // Return the list of products if found
	            return ResponseEntity.ok(products);
	        } else {
	            // Return NOT FOUND if user does not exist
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    }

	    // Return UNAUTHORIZED status if token validation fails
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}



}
