package com.microservice.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.model.MainCategory;
import com.microservice.productservice.model.SubCategory;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategory, Long> {
    List<SubCategory> findByMainCategory(MainCategory mainCategory);
    SubCategory findByCategoryName(String categoryName);
}
