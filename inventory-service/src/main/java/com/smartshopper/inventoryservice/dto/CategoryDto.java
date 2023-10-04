package com.smartshopper.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class CategoryDto {
	private MainCategoryDto mainCategory;
	private SubCategoryDto subCategory;
	private SecondaryCategoryDto secondaryCategory;
}
