package com.smartshopper.usercartservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
	private long categoryId;
	private String categoryName;
	@JsonIgnore
	private MainCategoryDto mainCategory;
//	private Set<MainCategory> mainCategory;
}
