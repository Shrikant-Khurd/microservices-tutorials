package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private long productId;
	private String productName;
	private String description;
	private double price;
	private String productStatus;
	private byte[] image;
	private CategoryDto category;
}
