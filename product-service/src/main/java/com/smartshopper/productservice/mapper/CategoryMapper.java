package com.smartshopper.productservice.mapper;

import com.smartshopper.productservice.dto.CategoryDto;
import com.smartshopper.productservice.dto.MainCategoryDto;
import com.smartshopper.productservice.dto.SecondaryCategoryDto;
import com.smartshopper.productservice.dto.SubCategoryDto;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.Product;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryMapper {

	public static CategoryDto mapToCategoryDto(Product productRequest) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setMainCategory(productRequest.getMainCategory());
		categoryDto.setSubCategory(productRequest.getSubCategory());
		categoryDto.setSecondaryCategory(productRequest.getSecondaryCategory());

		return categoryDto;
	}

	// Main Category
	// Convert MainCategory MongoDB Document into MainCategoryDto
	public static MainCategoryDto mapToMainCategoryDto(MainCategory mainCategory) {
		MainCategoryDto mainCategoryDto = new MainCategoryDto();
		mainCategoryDto.setCategoryId(mainCategory.getCategoryId());
		mainCategoryDto.setCategoryName(mainCategory.getCategoryName());
		return mainCategoryDto;
	}

	// Convert MainCategoryDto into MainCategory MongoDB Document
	public static MainCategory mapToMainCategory(MainCategoryDto mainCategoryDto) {
		MainCategory mainCategory = new MainCategory();
		mainCategory.setCategoryId(mainCategoryDto.getCategoryId());
		mainCategory.setCategoryName(mainCategoryDto.getCategoryName());
		return mainCategory;
	}

	// Sub Category
	// Convert SubCategory MongoDB Document into SubCategoryDto
	public static SubCategoryDto mapToSubCategoryDto(SubCategory subCategory) {
		SubCategoryDto subCategoryDto = new SubCategoryDto();
		subCategoryDto.setCategoryId(subCategory.getCategoryId());
		subCategoryDto.setCategoryName(subCategory.getCategoryName());
		subCategoryDto.setMainCategory(subCategory.getMainCategory());
		return subCategoryDto;
	}

	// Convert SubCategoryDto into SubCategory MongoDB Document
	public static SubCategory mapToSubCategory(SubCategoryDto subCategoryDto) {
		SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId(subCategoryDto.getCategoryId());
		subCategory.setCategoryName(subCategoryDto.getCategoryName());
		subCategory.setMainCategory(subCategoryDto.getMainCategory());
		return subCategory;
	}

	// Secondary Category
	// Convert SecondaryCategory MongoDB Document into SecondaryCategoryDto
	public static SecondaryCategoryDto mapToSecondaryCategoryDto(SecondaryCategory secondaryCategory) {
		SecondaryCategoryDto secondaryCategoryDto = new SecondaryCategoryDto();
		secondaryCategoryDto.setCategoryId(secondaryCategory.getCategoryId());
		secondaryCategoryDto.setCategoryName(secondaryCategory.getCategoryName());
		secondaryCategoryDto.setSubCategory(secondaryCategory.getSubCategory());
		return secondaryCategoryDto;
	}

	// Convert SecondaryCategoryDto into SecondaryCategory MongoDB Document
	public static SecondaryCategory mapToSecondaryCategory(SecondaryCategoryDto secondaryCategoryDto) {
		SecondaryCategory secondaryCategory = new SecondaryCategory();
		secondaryCategory.setCategoryId(secondaryCategoryDto.getCategoryId());
		secondaryCategory.setCategoryName(secondaryCategoryDto.getCategoryName());
		secondaryCategoryDto.setSubCategory(secondaryCategoryDto.getSubCategory());
		return secondaryCategory;
	}
}
