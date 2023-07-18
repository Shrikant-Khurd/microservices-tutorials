package com.mongodb.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.CategoryDto;
import com.mongodb.productservice.exception.CategoryAlreadyExistException;
import com.mongodb.productservice.exception.RecordNotFoundException;
import com.mongodb.productservice.model.Category;
import com.mongodb.productservice.service.CategoryService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping("/add/category")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryRequest) {

		ApiResponse savedUser = categoryService.addCategory(categoryRequest);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}

	@GetMapping("/all/category")
	public ResponseEntity<List<CategoryDto>> getAllCategory() {
		List<CategoryDto> categoryList = categoryService.getAllCategory();
		return new ResponseEntity<>(categoryList, HttpStatus.OK);
	}

	@GetMapping("/category/byId/{id}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable long id) {
		CategoryDto category = categoryService.getCategoryById(id);
		if (category == null)
			throw new RecordNotFoundException("category not found.");
		return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
	}

	@PostMapping("/update/category/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody Category category) {
		categoryService.updateCategory(id, category);
		return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
	}

}
