package com.microservice.productservice.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

	@Transient
	public static final String SEQUENCE_NAME = "product_sequence";

	@Id
	@Field("productId")
	private long productId;
	private String productName;
	private String description;
	private double price;
	private String productStatus;
	private byte[] image;
	private boolean productInventoryStatus;
	@DBRef
	private MainCategory mainCategory;
	@DBRef
	private SubCategory subCategory;
	@DBRef
	private SecondaryCategory secondaryCategory;
	private Set<String> sizes;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;

}