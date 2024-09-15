package com.rajesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rajesh.entity.Product;
import com.rajesh.feign.UserServiceFeignClient;
import com.rajesh.service.ProductService;

import java.util.List;

@RestController
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
	public ResponseEntity<String> addProduct(@RequestHeader("Authorization") String token,
			@RequestBody Product product) {
		Boolean isValid = userClient.validateToken(token);
		if (isValid) {
			String roles = userClient.getUserRoles(token);
			if (roles.contains(role_seller)) {
				productService.addProduct(product);
				return ResponseEntity.ok("Product added successfully");
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add products");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
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
}
