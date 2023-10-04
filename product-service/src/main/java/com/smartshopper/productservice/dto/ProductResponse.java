package com.smartshopper.productservice.dto;

import java.util.Set;

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
public class ProductResponse {
	private long productId;
	private String productName;
	private String description;
	private double price;
	private String productStatus;
	private byte[] image;
	private MainCategory category;
	private SubCategory subCategory;
	private SecondaryCategory secondaryCategory;
	private Set<String> size;
}