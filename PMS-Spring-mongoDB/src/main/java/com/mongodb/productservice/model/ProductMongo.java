package com.mongodb.productservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductMongo {

	@Transient
	public static final String SEQUENCE_NAME="product_sequence";
	
	@Id	
	@Field("productId")
	private long productId;
    private String productName;
    private double price;
    private byte[] image;
//    private String image;
}