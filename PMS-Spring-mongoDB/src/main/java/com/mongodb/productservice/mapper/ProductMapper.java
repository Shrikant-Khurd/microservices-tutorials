package com.mongodb.productservice.mapper;

import com.mongodb.productservice.dto.ProductDto;
import com.mongodb.productservice.model.Product;

public class ProductMapper {

	 // Convert Product JPA Entity into ProductDto
	public static ProductDto mapToProductDto(Product productRequest) {
		ProductDto productDto = new ProductDto();
		productDto.setProductId(productRequest.getId());
		productDto.setProductName(productRequest.getName());
		//productDto.setDescription(productRequest.getDescription());
		productDto.setPrice(productRequest.getPrice());
	//	productDto.setCreatedByUserId(productRequest.getCreatedByUserId());

		
		//productDto.setImage(productRequest.getImage());
		return productDto;
	}
	
	// Convert ProductDto into Product JPA Entity
	public static Product mapToProduct(ProductDto productRequest) {
		Product productDto = new Product();
//		productDto.setProductId(productRequest.getId());
		productDto.setName(productRequest.getProductName());
	//	productDto.setDescription(productRequest.getDescription());
		//productDto.setPrice(productRequest.getPrice());
		//productDto.setCreatedByUserId(productRequest.getCreatedByUserId());

	//	productDto.setImage(productRequest.getImage());
		return productDto;
	}
}
