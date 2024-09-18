package com.rajesh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private Double price;
	private Integer quantity;
	private String description;
	// Store the userId as a foreign key or reference
    private Long userId;

	// Getters and Setters
}
