package com.smartshopper.productservice.mapper;

import com.smartshopper.productservice.dto.CategoryDto;
import com.smartshopper.productservice.dto.ProductDto;
import com.smartshopper.productservice.model.Product;

public class ProductMapper {

	// Convert Product JPA Entity into ProductDto
	public static ProductDto mapToProductDto(Product productRequest) {
		ProductDto productDto = new ProductDto();

		CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(productRequest);
//		categoryDto.setMainCategory(productRequest.getMainCategory());
//		categoryDto.setSubCategory(productRequest.getSubCategory());
//		categoryDto.setSecondaryCategory(productRequest.getSecondaryCategory());

		productDto.setCategory(categoryDto);
		productDto.setProductId(productRequest.getProductId());
		productDto.setProductName(productRequest.getProductName());
		productDto.setDescription(productRequest.getDescription());
		productDto.setPrice(productRequest.getPrice());
		productDto.setProductStatus(productRequest.getProductStatus());
//		productDto.setMainCategory(productRequest.getMainCategory());
//		productDto.setSubCategory(productRequest.getSubCategory());
//		productDto.setSecondaryCategory(productRequest.getSecondaryCategory());
		productDto.setImage(productRequest.getImage());
		productDto.setSizes(productRequest.getSizes());
		productDto.setCreatedAt(productRequest.getCreatedAt());
		productDto.setUpdatedAt(productRequest.getUpdatedAt());
		return productDto;
	}

	// Convert ProductDto into Product JPA Entity
	public static Product mapToProduct(ProductDto productRequest) {
		Product productDto = new Product();
//		productDto.setProductId(productRequest.getId());
		productDto.setProductName(productRequest.getProductName());
		productDto.setDescription(productRequest.getDescription());
		productDto.setPrice(productRequest.getPrice());
		productDto.setProductStatus(productRequest.getProductStatus());
//		productDto.setMainCategory(productRequest.getMainCategory());
//		productDto.setSubCategory(productRequest.getSubCategory());
//		productDto.setSecondaryCategory(productRequest.getSecondaryCategory());
		productDto.setImage(productRequest.getImage());
		productDto.setSizes(productRequest.getSizes());
		productDto.setCreatedAt(productRequest.getCreatedAt());
		productDto.setUpdatedAt(productRequest.getUpdatedAt());
		return productDto;
	}
}
