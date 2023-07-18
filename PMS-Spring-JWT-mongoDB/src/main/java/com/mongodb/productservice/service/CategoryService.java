package com.mongodb.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.CategoryDto;
import com.mongodb.productservice.exception.CategoryAlreadyExistException;
import com.mongodb.productservice.model.Category;
import com.mongodb.productservice.repository.CategoryRepository;
import com.mongodb.productservice.utils.ConstantMethods;
import com.mongodb.productservice.utils.SequenceGenaratorService;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private MessageSource messageSource;
	
	public ApiResponse addCategory(CategoryDto categoryRequest) {
		
		if (findByCategoryName(categoryRequest.getCategoryName()))
		{
		Category saveCategory = new Category();
		categoryRequest.setCategoryId(sequenceGenaratorService.generateSequence(Category.SEQUENCE_NAME));

		saveCategory.setCategoryId(categoryRequest.getCategoryId());
		saveCategory.setCategoryName(categoryRequest.getCategoryName());
		Category savedCategory= categoryRepository.save(saveCategory);
		return ConstantMethods.successResponse(savedCategory,
				messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH));
		}
		else {
			throw new CategoryAlreadyExistException("Category already exist.");
		}
	}

	public boolean findByCategoryName(String categoryName) {
		Category category = categoryRepository.findByCategoryName(categoryName);
		if (category == null)
			return true;
		else
			return false;
	}

	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		List<CategoryDto> categoryDtos = new ArrayList<>();

		for (Category category : categories) {
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setCategoryId(category.getCategoryId());
			categoryDto.setCategoryName(category.getCategoryName());

			categoryDtos.add(categoryDto);
		}
		return categoryDtos;
	}

	public CategoryDto getCategoryById(long id) {
		Category category=categoryRepository.findById(id).orElse(null);
		
		 return CategoryDto.builder()
	                .categoryId(category.getCategoryId())
	                .categoryName(category.getCategoryName()) 
	                .build();
	}



	public void updateCategory(long id, Category category) {
		Category categoryDetails = categoryRepository.findById(id).get();

		if (category.getCategoryName() != null)
			categoryDetails.setCategoryName(category.getCategoryName());
		categoryRepository.save(categoryDetails);

	}

}
