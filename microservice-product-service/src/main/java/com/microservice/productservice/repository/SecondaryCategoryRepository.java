package com.microservice.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.model.SecondaryCategory;
import com.microservice.productservice.model.SubCategory;

@Repository
public interface SecondaryCategoryRepository extends MongoRepository<SecondaryCategory, Long> {
    List<SecondaryCategory> findBySubCategory(SubCategory subCategory);
    SecondaryCategory findByCategoryName(String categoryName);
}
