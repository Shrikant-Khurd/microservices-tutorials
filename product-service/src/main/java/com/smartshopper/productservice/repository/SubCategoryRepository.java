package com.smartshopper.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.SubCategory;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategory, Long> {
    List<SubCategory> findByMainCategory(MainCategory mainCategory);
    SubCategory findByCategoryName(String categoryName);
}
