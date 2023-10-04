package com.smartshopper.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;

@Repository
public interface SecondaryCategoryRepository extends MongoRepository<SecondaryCategory, Long> {
    List<SecondaryCategory> findBySubCategory(SubCategory subCategory);
    SecondaryCategory findByCategoryName(String categoryName);
}
