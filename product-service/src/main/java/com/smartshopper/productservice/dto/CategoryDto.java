package com.smartshopper.productservice.dto;

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
public class CategoryDto {
	private MainCategory mainCategory;
	private SubCategory subCategory;
	private SecondaryCategory secondaryCategory;
}
