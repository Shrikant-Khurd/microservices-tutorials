package com.smartshopper.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartshopper.productservice.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecondaryCategoryDto {
	private long categoryId;
	private String categoryName;
//	@JsonIgnore
	private SubCategory subCategory;
}
