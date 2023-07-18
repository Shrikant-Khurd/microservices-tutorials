package com.microservice.productservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.CategoryDto;
import com.microservice.productservice.exception.CategoryAlreadyExistException;
import com.microservice.productservice.model.Category;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.CategoryRepository;
import com.microservice.productservice.utils.ConstantMethods;
import com.microservice.productservice.utils.SequenceGenaratorService;

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
		Category Category = categoryRepository.findByCategoryName(categoryName);
		if (Category == null)
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

	public ApiResponse deleteCategory(long id) {
		Category categoryDetails = categoryRepository.findById(id).get();
//		product.setUpdatedAt(Instant.now());
//		product.setProductStatus("DELETED");
//		categoryRepository.save(categoryDetails);
		categoryRepository.delete(categoryDetails);
		return ConstantMethods.successResponse(categoryDetails,
				messageSource.getMessage("api.response.category.delete", null, Locale.ENGLISH), HttpStatus.OK);
	}

}
