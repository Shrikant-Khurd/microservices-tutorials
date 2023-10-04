package com.smartshopper.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.smartshopper.productservice.dto.ApiResponse;
import com.smartshopper.productservice.dto.MainCategoryDto;
import com.smartshopper.productservice.dto.SecondaryCategoryDto;
import com.smartshopper.productservice.dto.SubCategoryDto;
import com.smartshopper.productservice.exception.CategoryAlreadyExistException;
import com.smartshopper.productservice.mapper.CategoryMapper;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;
import com.smartshopper.productservice.repository.MainCategoryRepository;
import com.smartshopper.productservice.repository.SecondaryCategoryRepository;
import com.smartshopper.productservice.repository.SubCategoryRepository;
import com.smartshopper.productservice.utils.ConstantMethods;
import com.smartshopper.productservice.utils.SequenceGeneratorService;

@Service
public class CategoryService {
    //    @Autowired
    private MainCategoryRepository mainCategoryRepository;
    //    @Autowired
    private SubCategoryRepository subCategoryRepository;
    //    @Autowired
    private SecondaryCategoryRepository secondaryCategoryRepository;
    //    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    private MessageSource messageSource;

    @Autowired
    public CategoryService(MainCategoryRepository mainCategoryRepository, SubCategoryRepository subCategoryRepository, SecondaryCategoryRepository secondaryCategoryRepository, SequenceGeneratorService sequenceGeneratorService, MessageSource messageSource) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.secondaryCategoryRepository = secondaryCategoryRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.messageSource = messageSource;
    }

    // Main Category
    public ApiResponse addMainCategory(MainCategoryDto categoryRequest) {
        if (findByMainCategoryName(categoryRequest.getCategoryName())) {
            MainCategory saveCategory = new MainCategory();
            categoryRequest.setCategoryId(sequenceGeneratorService.generateSequence(MainCategory.SEQUENCE_NAME));
            saveCategory.setCategoryId(categoryRequest.getCategoryId());
            saveCategory.setCategoryName(categoryRequest.getCategoryName());
            MainCategory savedCategory = mainCategoryRepository.save(saveCategory);

            return ConstantMethods.successResponse(savedCategory,
                    messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH), HttpStatus.CREATED);
        } else {
            throw new CategoryAlreadyExistException(messageSource.getMessage("api.error.mainCategory.already.exists", null, Locale.ENGLISH));
        }
    }

    public boolean findByMainCategoryName(String categoryName) {
        MainCategory category = mainCategoryRepository.findByCategoryName(categoryName);
        if (category == null)
            return true;
        else
            return false;
    }

    public List<MainCategoryDto> getAllMainCategories() {
        List<MainCategory> categories = mainCategoryRepository.findAll();
        List<MainCategoryDto> categoryDtos = new ArrayList<>();
        for (MainCategory category : categories) {
            MainCategoryDto categoryDto = new MainCategoryDto();
            categoryDto.setCategoryId(category.getCategoryId());
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public MainCategoryDto getMainCategoryById(long id) {
        MainCategory category = mainCategoryRepository.findById(id).orElse(null);

        return CategoryMapper.mapToMainCategoryDto(category);
    }

    public ApiResponse updateMainCategory(long id, MainCategoryDto categoryDto) {
        MainCategory categoryDetails = mainCategoryRepository.findById(id).get();
        if (categoryDto.getCategoryName() != null)
            categoryDetails.setCategoryName(categoryDto.getCategoryName());
        mainCategoryRepository.save(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.update", null, Locale.ENGLISH), HttpStatus.OK);
    }

    public ApiResponse deleteMainCategoryById(long id) {
        MainCategory categoryDetails = mainCategoryRepository.findById(id).get();
        mainCategoryRepository.delete(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.delete", null, Locale.ENGLISH), HttpStatus.OK);
    }

    // Sub Category
    public ApiResponse addSubCategory(SubCategoryDto categoryRequest) {
//		if (findBySubCategoryName(categoryRequest.getCategoryName())) {
        SubCategory saveCategory = new SubCategory();
        categoryRequest.setCategoryId(sequenceGeneratorService.generateSequence(SubCategory.SEQUENCE_NAME));
        saveCategory.setCategoryId(categoryRequest.getCategoryId());
        saveCategory.setCategoryName(categoryRequest.getCategoryName());
        saveCategory.setMainCategory(categoryRequest.getMainCategory());
        SubCategory savedCategory = subCategoryRepository.save(saveCategory);
        SubCategoryDto subCategoryDto = CategoryMapper.mapToSubCategoryDto(savedCategory);
        return ConstantMethods.successResponse(subCategoryDto,
                messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH), HttpStatus.CREATED);
//		} else {
//			throw new CategoryAlreadyExistException("Category already exist.");
//		}
    }

    public boolean findBySubCategoryName(String categoryName) {
        SubCategory category = subCategoryRepository.findByCategoryName(categoryName);
        if (category == null)
            return true;
        else
            return false;
    }

    public List<SubCategoryDto> getAllSubCategories() {
        List<SubCategory> categories = subCategoryRepository.findAll();
        List<SubCategoryDto> categoryDtos = new ArrayList<>();
        for (SubCategory category : categories) {
            SubCategoryDto categoryDto = new SubCategoryDto();
            categoryDto.setCategoryId(category.getCategoryId());
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDto.setMainCategory(category.getMainCategory());
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public SubCategoryDto getSubCategoryById(long id) {
        SubCategory category = subCategoryRepository.findById(id).orElse(null);
        return CategoryMapper.mapToSubCategoryDto(category);
    }

    public ApiResponse updateSubCategory(long id, SubCategoryDto categoryDto) {
        SubCategory categoryDetails = subCategoryRepository.findById(id).get();
        if (categoryDto.getCategoryName() != null)
            categoryDetails.setCategoryName(categoryDto.getCategoryName());
        subCategoryRepository.save(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.update", null, Locale.ENGLISH), HttpStatus.OK);
    }

    public ApiResponse deleteSubCategoryById(long id) {
        SubCategory categoryDetails = subCategoryRepository.findById(id).get();
        subCategoryRepository.delete(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.delete", null, Locale.ENGLISH), HttpStatus.OK);
    }

    // Secondary Category
    public ApiResponse addSecondaryCategory(SecondaryCategoryDto categoryRequest) {
//        if (findBySecondaryCategoryName(categoryRequest.getCategoryName())) {
        SecondaryCategory saveCategory = new SecondaryCategory();
        categoryRequest.setCategoryId(sequenceGeneratorService.generateSequence(SecondaryCategory.SEQUENCE_NAME));
        saveCategory.setCategoryId(categoryRequest.getCategoryId());
        saveCategory.setCategoryName(categoryRequest.getCategoryName());
        saveCategory.setSubCategory(categoryRequest.getSubCategory());
        SecondaryCategory savedCategory = secondaryCategoryRepository.save(saveCategory);
        SecondaryCategoryDto subCategoryDto = CategoryMapper.mapToSecondaryCategoryDto(savedCategory);
        return ConstantMethods.successResponse(subCategoryDto,
                messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH), HttpStatus.CREATED);
//        } else {
//            throw new CategoryAlreadyExistException("Category already exist.");
//        }
    }

    public boolean findBySecondaryCategoryName(String categoryName) {
        SecondaryCategory category = secondaryCategoryRepository.findByCategoryName(categoryName);
        if (category == null)
            return true;
        else
            return false;
    }

    public List<SecondaryCategoryDto> getAllSecondaryCategories() {
        List<SecondaryCategory> categories = secondaryCategoryRepository.findAll();
        List<SecondaryCategoryDto> categoryDtos = new ArrayList<>();
        for (SecondaryCategory category : categories) {
            SecondaryCategoryDto categoryDto = new SecondaryCategoryDto();
            categoryDto.setCategoryId(category.getCategoryId());
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDto.setSubCategory(category.getSubCategory());
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public SecondaryCategoryDto getSecondaryCategoryById(long id) {
        SecondaryCategory category = secondaryCategoryRepository.findById(id).orElse(null);
        return CategoryMapper.mapToSecondaryCategoryDto(category);
    }

    public ApiResponse updateSecondaryCategory(long id, SecondaryCategoryDto categoryDto) {
        SecondaryCategory categoryDetails = secondaryCategoryRepository.findById(id).get();
        if (categoryDto.getCategoryName() != null)
            categoryDetails.setCategoryName(categoryDto.getCategoryName());
        secondaryCategoryRepository.save(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.update", null, Locale.ENGLISH), HttpStatus.OK);
    }

    public ApiResponse deleteSecondaryCategoryById(long id) {
        SecondaryCategory categoryDetails = secondaryCategoryRepository.findById(id).get();
        secondaryCategoryRepository.delete(categoryDetails);
        return ConstantMethods.successResponse(categoryDetails,
                messageSource.getMessage("api.response.category.delete", null, Locale.ENGLISH), HttpStatus.OK);
    }

    public ApiResponse getSubCategoriesByMainCategory(MainCategoryDto requestCategory) {
        MainCategory category = CategoryMapper.mapToMainCategory(requestCategory);
        List<SubCategory> categoryList = subCategoryRepository.findByMainCategory(category);

        if (categoryList.isEmpty()) {
            return ConstantMethods.successResponse(categoryList,
                    messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH));
        }

        List<SubCategoryDto> subCategoryDto = new ArrayList<>();
        for (SubCategory subCategory : categoryList) {
            SubCategoryDto dto = CategoryMapper.mapToSubCategoryDto(subCategory);
            subCategoryDto.add(dto);
        }

        return ConstantMethods.successResponse(subCategoryDto,
                messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH));
    }

    public ApiResponse getSecondaryCategoriesBySubCategory(Long subCategoryId) {

        SubCategoryDto requestCategory = new SubCategoryDto();
        requestCategory.setCategoryId(subCategoryId);
        SubCategory category = CategoryMapper.mapToSubCategory(requestCategory);
        List<SecondaryCategory> categoryList = secondaryCategoryRepository.findBySubCategory(category);
        List<SecondaryCategoryDto> secondaryCategoryDto = new ArrayList<>();
        for (SecondaryCategory subCategory : categoryList) {
            SecondaryCategoryDto dto = CategoryMapper.mapToSecondaryCategoryDto(subCategory);
            secondaryCategoryDto.add(dto);
        }
        return ConstantMethods.successResponse(secondaryCategoryDto,
                messageSource.getMessage("api.response.category.added", null, Locale.ENGLISH));
    }

    public List<SecondaryCategoryDto> getSecondaryCategoriesBySubCategorys(Long subCategoryId) {

        SubCategoryDto requestCategory = new SubCategoryDto();
        requestCategory.setCategoryId(subCategoryId);
        SubCategory category = CategoryMapper.mapToSubCategory(requestCategory);
        List<SecondaryCategory> categoryList = secondaryCategoryRepository.findBySubCategory(category);
        List<SecondaryCategoryDto> secondaryCategoryDto = new ArrayList<>();
        for (SecondaryCategory subCategory : categoryList) {
            SecondaryCategoryDto dto = CategoryMapper.mapToSecondaryCategoryDto(subCategory);
            secondaryCategoryDto.add(dto);
        }
        return secondaryCategoryDto;
    }
}
