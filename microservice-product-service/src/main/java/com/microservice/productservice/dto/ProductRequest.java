package com.microservice.productservice.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.microservice.productservice.model.MainCategory;
import com.microservice.productservice.model.SecondaryCategory;
import com.microservice.productservice.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private long productId;
	private String productName;
	private String description;
	private double price;
	private String productStatus;
	private MultipartFile image;
	private MainCategory mainCategory;
	private SubCategory subCategory;
	private SecondaryCategory secondaryCategory;
	private Set<String> sizes;
	private Integer quantityInStock;
}