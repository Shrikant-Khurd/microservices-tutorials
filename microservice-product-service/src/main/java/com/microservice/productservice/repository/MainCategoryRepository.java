package com.microservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.model.MainCategory;

@Repository
public interface MainCategoryRepository extends MongoRepository<MainCategory, Long> {
	MainCategory findByCategoryName(String categoryName);
}
