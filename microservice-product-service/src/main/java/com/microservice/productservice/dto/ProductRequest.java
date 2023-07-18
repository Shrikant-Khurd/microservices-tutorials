package com.microservice.productservice.dto;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.microservice.productservice.model.Category;

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
	private Category category;
}