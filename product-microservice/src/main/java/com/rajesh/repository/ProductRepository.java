package com.rajesh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajesh.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByUserId(Long id);
}
