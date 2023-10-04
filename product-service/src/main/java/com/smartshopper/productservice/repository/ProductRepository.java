package com.smartshopper.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.Product;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;

public interface ProductRepository extends MongoRepository<Product, Long> {

//	List<Product> findByCategory(MainCategory categoryname);

	List<Product> findByMainCategory(MainCategory mainCategory);

	List<Product> findBySubCategory(SubCategory subCategory);

	List<Product> findBySecondaryCategory(SecondaryCategory secondaryCategory);

}
