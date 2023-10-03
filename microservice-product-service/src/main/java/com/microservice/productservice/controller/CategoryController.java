package com.microservice.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.MainCategoryDto;
import com.microservice.productservice.dto.SecondaryCategoryDto;
import com.microservice.productservice.dto.SubCategoryDto;
import com.microservice.productservice.exception.RecordNotFoundException;
import com.microservice.productservice.model.SubCategory;
import com.microservice.productservice.service.CategoryService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	// Main Cateogry API's
	@PostMapping("/add/mainCtegory")
	public ResponseEntity<ApiResponse> addMainCategory(@RequestBody MainCategoryDto categoryRequest) {
		ApiResponse savedUser = categoryService.addMainCategory(categoryRequest);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}

	@GetMapping("/mainCategories")
	public ResponseEntity<List<MainCategoryDto>> getAllMainCategories() {
		List<MainCategoryDto> categoryList = categoryService.getAllMainCategories();
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	@GetMapping("/mainCategory/byId/{id}")
	public ResponseEntity<MainCategoryDto> getMainCategory(@PathVariable long id) {
		MainCategoryDto category = categoryService.getMainCategoryById(id);
		if (category == null)
			throw new RecordNotFoundException("category not found.");
		return new ResponseEntity<>(categoryService.getMainCategoryById(id), HttpStatus.OK);
	}

	@PostMapping("/mainCategory/update/{id}")
	public ResponseEntity<ApiResponse> updateMainCategory(@PathVariable int id,
			@RequestBody MainCategoryDto categoryDto) {
		ApiResponse response = categoryService.updateMainCategory(id, categoryDto);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@PostMapping("/mainCategory/delete/{id}")
	public ResponseEntity<ApiResponse> deleteMainCategory(@PathVariable long id) {
		ApiResponse response = categoryService.deleteMainCategory(id);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}


	// Sub Cateogry API's
	@PostMapping("/add/subCategory")
	public ResponseEntity<ApiResponse> addSubCategory(@RequestBody SubCategoryDto categoryRequest) {
		ApiResponse savedUser = categoryService.addSubCategory(categoryRequest);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}

	@GetMapping("/subCategories")
	public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
		List<SubCategoryDto> categoryList = categoryService.getAllSubCategories();
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	@GetMapping("/subCategory/byId/{id}")
	public ResponseEntity<SubCategoryDto> getSubCategory(@PathVariable long id) {
		SubCategoryDto category = categoryService.getSubCategoryById(id);
		if (category == null)
			throw new RecordNotFoundException("category not found.");
		return new ResponseEntity<>(categoryService.getSubCategoryById(id), HttpStatus.OK);
	}

	@PostMapping("/subCategory/update/{id}")
	public ResponseEntity<ApiResponse> updateSubCategory(@PathVariable int id,
			@RequestBody SubCategoryDto categoryDto) {
		ApiResponse response = categoryService.updateSubCategory(id, categoryDto);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@PostMapping("/subCategory/delete/{id}")
	public ResponseEntity<ApiResponse> deleteSubCategory(@PathVariable long id) {
		ApiResponse response = categoryService.deleteSubCategoryById(id);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	// Secondary Cateogry API's
	@PostMapping("/add/secondaryCategory")
	public ResponseEntity<ApiResponse> addSecondaryCategory(@RequestBody SecondaryCategoryDto categoryRequest) {
		ApiResponse savedUser = categoryService.addSecondaryCategory(categoryRequest);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}

	@GetMapping("/secondaryCategories")
	public ResponseEntity<List<SecondaryCategoryDto>> getAllSecondaryCategories() {
		List<SecondaryCategoryDto> categoryList = categoryService.getAllSecondaryCategories();
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	@GetMapping("/secondaryCategory/byId/{id}")
	public ResponseEntity<SecondaryCategoryDto> getSecondaryCtegory(@PathVariable long id) {
		SecondaryCategoryDto category = categoryService.getSecondaryCategoryById(id);
		if (category == null)
			throw new RecordNotFoundException("category not found.");
		return new ResponseEntity<>(categoryService.getSecondaryCategoryById(id), HttpStatus.OK);
	}

	@PostMapping("/secondaryCategory/update/{id}")
	public ResponseEntity<ApiResponse> updateSecondaryCategory(@PathVariable int id,
			@RequestBody SecondaryCategoryDto categoryDto) {
		ApiResponse response = categoryService.updateSecondaryCategory(id, categoryDto);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@PostMapping("/secondaryCategory/delete/{id}")
	public ResponseEntity<ApiResponse> deleteSecondaryCategory(@PathVariable long id) {
		ApiResponse response = categoryService.deleteSecondaryCategoryById(id);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}
	
	
//	@GetMapping("/subcategories")
//	public ResponseEntity<ApiResponse> getSubCategoriesByMainCategory(@ModelAttribute MainCategoryDto requestCategory) {
//		ApiResponse response = categoryService.getSubCategoriesByMainCategory(requestCategory);
//		return new ResponseEntity<>(response, response.getHttpStatus());
//	}
	
	@GetMapping("/main-categories/{mainCategoryId}/sub-categories")
	public ResponseEntity<ApiResponse> getSubCategoriesByMainCategory(@PathVariable("mainCategoryId") Long mainCategoryId) {
		MainCategoryDto mainCategory = new MainCategoryDto();
		mainCategory.setCategoryId(mainCategoryId);
		ApiResponse response = categoryService.getSubCategoriesByMainCategory(mainCategory);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}
	@GetMapping("/sub-categories/{subCategoryId}/secondary-categories")
	public ResponseEntity<ApiResponse> getSecondaryCategoriesBySubCategory(@PathVariable("subCategoryId") Long subCategoryId) {
//		 SubCategoryDto subCategory = new SubCategoryDto();
//	        subCategory.setCategoryId(subCategoryId);
		ApiResponse response = categoryService.getSecondaryCategoriesBySubCategory(subCategoryId);
//		ApiResponse response = categoryService.getSecondaryCategoriesBySubCategory(subCategory);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}
}
