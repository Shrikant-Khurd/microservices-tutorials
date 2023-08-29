package com.microservice.usercartservice.dto;

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
	private long price;
	private long quantity;
	private CategoryDto category;

//	private long amount;
	private byte[] image;
}