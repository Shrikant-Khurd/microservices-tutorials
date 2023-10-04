package com.smartshopper.productservice.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartshopper.productservice.model.MainCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
	private long categoryId;
	private String categoryName;
//	@JsonIgnore
	private MainCategory mainCategory;
//	private Set<MainCategory> mainCategory;
}
