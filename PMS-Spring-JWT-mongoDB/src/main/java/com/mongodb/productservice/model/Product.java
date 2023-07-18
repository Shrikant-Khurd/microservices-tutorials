package com.mongodb.productservice.model;

import java.time.Instant;

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

@Document(value="products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
	
	@Transient
	public static final String SEQUENCE_NAME="product_sequence";
	
	@Id
	@Field("productId")
	private long productId;
    private String name;
    private String description;
    private double price;
    private String productStatus;
    private byte[] image;
    private String imagePath;
    @DBRef
	private Category category;
    @CreatedDate
	private Instant createdAt;
	@LastModifiedDate
	private Instant updatedAt;
   
}