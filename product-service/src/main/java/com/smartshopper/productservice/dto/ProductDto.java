package com.smartshopper.productservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ProductDto {
	private long productId;
	private String productName;
	private String description;
	private double price;
	private String productStatus;
	private byte[] image;
	private CategoryDto category;
	private MainCategory mainCategory;
	private SubCategory subCategory;
	private SecondaryCategory secondaryCategory;
	private Set<String> sizes;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt;

	private Integer quantityInStock;
	private boolean productInventoryStatus;
}