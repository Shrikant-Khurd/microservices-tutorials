package com.mongodb.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.productservice.model.ProductMongo;

public interface ProductMongoRepository extends MongoRepository<ProductMongo, Long> {

	//Product findById(String id);

}
