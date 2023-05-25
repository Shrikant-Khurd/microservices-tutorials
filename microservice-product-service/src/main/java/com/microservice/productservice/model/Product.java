package com.microservice.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value="product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
	
	@Transient
	public static final String SEQUENCE_NAME="product_sequence";
	
	@Id
    private long id;
    private String name;
    private String description;
    private long price;
    private long createdByUserId;
//    private UserDto userDetails;
   
}
