package com.rajesh.dto;

import lombok.Data;

@Data
public class ProductDTO {
	private String name;
	private Double price;
	private Integer quantity;
	private String description;
	// Store the userId as a foreign key or reference
    private Long userId;

	// Getters and Setters
}
