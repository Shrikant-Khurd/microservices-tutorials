package com.microservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, Long> {
	
	
	Category findByCategoryName(String categoryName);
	
	
}
