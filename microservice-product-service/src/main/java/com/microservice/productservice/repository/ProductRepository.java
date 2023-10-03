package com.microservice.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.productservice.model.MainCategory;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.model.SecondaryCategory;
import com.microservice.productservice.model.SubCategory;

public interface ProductRepository extends MongoRepository<Product, Long> {

//	List<Product> findByCategory(MainCategory categoryname);

	List<Product> findByMainCategory(MainCategory mainCategory);

	List<Product> findBySubCategory(SubCategory subCategory);

	List<Product> findBySecondaryCategory(SecondaryCategory secondaryCategory);

}
