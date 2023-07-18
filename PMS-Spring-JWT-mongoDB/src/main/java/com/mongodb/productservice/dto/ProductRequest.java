package com.mongodb.productservice.dto;

import org.springframework.web.multipart.MultipartFile;

import com.mongodb.productservice.model.Category;

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
//	 private String imagePath;
	private Category category;
}