package com.microservice.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private long productId;
	private String productName;
	private String description;
	private double price;
	private CategoryDto category;
}