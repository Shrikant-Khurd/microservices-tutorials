package com.microservice.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.productservice.model.Category;
import com.microservice.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, Long> {

	// Product findById(String id);
	List<Product> findByCategory(Category categoryname);
	List<Product> findByCategory(long categoryname);
}
