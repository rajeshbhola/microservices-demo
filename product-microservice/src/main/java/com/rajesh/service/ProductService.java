package com.rajesh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajesh.dto.ProductDTO;
import com.rajesh.entity.Product;
import com.rajesh.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	public Product createProduct(ProductDTO productDto, Long userId) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setUserId(userId); // Link userId to product

        return productRepository.save(product);
    }

    public List<Product> getProducts(Long userId) {
        return productRepository.findByUserId(userId);
    }
}
