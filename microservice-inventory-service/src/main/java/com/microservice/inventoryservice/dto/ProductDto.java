package com.microservice.inventoryservice.dto;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
	//private byte[] image;
	private CategoryDto category;
}