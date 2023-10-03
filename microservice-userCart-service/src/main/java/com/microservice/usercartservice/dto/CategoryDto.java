package com.microservice.usercartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	private MainCategoryDto mainCategory;
	private SubCategoryDto subCategory;
	private SecondaryCategoryDto secondaryCategory;
}
