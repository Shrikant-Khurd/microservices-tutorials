package com.microservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, Long> {

	//Product findById(String id);

}
