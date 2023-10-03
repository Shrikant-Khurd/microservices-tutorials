package com.microservice.inventoryservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
//	private SubCategoryDto subCategory;
}
