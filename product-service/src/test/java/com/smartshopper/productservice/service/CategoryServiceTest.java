package com.smartshopper.productservice.service;

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
import com.smartshopper.productservice.utils.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private MainCategoryRepository mainCategoryRepository;
    @Mock
    private SubCategoryRepository subCategoryRepository;
    @Mock
    private SecondaryCategoryRepository secondaryCategoryRepository;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService; //= mock(SequenceGeneratorService.class);
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CategoryService categoryService;

    // Main Category Test Cases
    @Test
    void testAddMainCategory_Success() {
        MainCategoryDto categoryRequest = new MainCategoryDto();
        categoryRequest.setCategoryName("Test Main Category");

        when(mainCategoryRepository.findByCategoryName(categoryRequest.getCategoryName())).thenReturn(null);
        when(sequenceGeneratorService.generateSequence(MainCategory.SEQUENCE_NAME)).thenReturn(1L);

        ApiResponse response = categoryService.addMainCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
    }

    @Test
    void testAddMainCategory_ThrowsCategoryAlreadyExistException() {

        MainCategoryDto categoryRequest = new MainCategoryDto();
        categoryRequest.setCategoryName("Test Main Category");

        MainCategory existingCategory = new MainCategory();
        existingCategory.setCategoryId(1L);
        existingCategory.setCategoryName(categoryRequest.getCategoryName());

        when(mainCategoryRepository.findByCategoryName(categoryRequest.getCategoryName())).thenReturn(existingCategory);

        CategoryAlreadyExistException exception = assertThrows(CategoryAlreadyExistException.class, () -> categoryService.addMainCategory(categoryRequest));
        assertNotNull(exception);
        System.out.println(exception.getMessage());
    }

    @Test
    void testFindByMainCategoryName_CategoryExists() {
        String categoryName = "Test Main Category";
        MainCategory existingCategory = new MainCategory();
        existingCategory.setCategoryId(1L);
        existingCategory.setCategoryName(categoryName);

        when(mainCategoryRepository.findByCategoryName(categoryName)).thenReturn(existingCategory);

        boolean result = categoryService.findByMainCategoryName(categoryName);
        assertFalse(result);
    }

    @Test
    void testFindByMainCategoryName_CategoryDoesNotExists() {
        String categoryName = "Test Main Category";
        MainCategory existingCategory = new MainCategory();
        existingCategory.setCategoryId(1L);
        existingCategory.setCategoryName(categoryName);

        when(mainCategoryRepository.findByCategoryName(categoryName)).thenReturn(null);

        boolean result = categoryService.findByMainCategoryName(categoryName);
        assertTrue(result);
    }

    @Test
    void testGetMainCategoryById() {
        long categoryId = 1L;
        MainCategory mainCategory = new MainCategory();
        mainCategory.setCategoryId(categoryId);
        mainCategory.setCategoryName("Test Main Category");

        when(mainCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mainCategory));

        MainCategoryDto result = categoryService.getMainCategoryById(categoryId);
        assertNotNull(result);
        assertEquals(result.getCategoryId(), mainCategory.getCategoryId());
    }

    @Test
    void testGetAllMainCategories_EmptyList() {
        List<MainCategory> categories = new ArrayList<>();
        when(mainCategoryRepository.findAll()).thenReturn(categories);
        List<MainCategoryDto> actualCategoryDtos = categoryService.getAllMainCategories();
        assertTrue(actualCategoryDtos.isEmpty());
    }

    @Test
    void testGetAllMainCategories() {
        List<MainCategory> categories = new ArrayList<>();
        categories.add(new MainCategory(1L, "Main Category 1"));
        categories.add(new MainCategory(2L, "Main Category 2"));

        when(mainCategoryRepository.findAll()).thenReturn(categories);

        List<MainCategoryDto> expectedCategoryDtos = new ArrayList<>();
        for (MainCategory category : categories) {
//            MainCategoryDto categoryDto = new MainCategoryDto();
//            categoryDto.setCategoryId(category.getCategoryId());
//            categoryDto.setCategoryName(category.getCategoryName());
            MainCategoryDto categoryDto =CategoryMapper.mapToMainCategoryDto(category);
            expectedCategoryDtos.add(categoryDto);
        }

        List<MainCategoryDto> actualCategoryDtos = categoryService.getAllMainCategories();

        assertEquals(expectedCategoryDtos, actualCategoryDtos);
    }

    @Test
    void testUpdateMainCategory_Success() {
        long categoryId = 1L;
        MainCategory existingCategory = new MainCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Main Category");

        MainCategoryDto categoryDto = new MainCategoryDto();
        categoryDto.setCategoryName("Updated Main Category");

        when(mainCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.updateMainCategory(categoryId, categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryDto.getCategoryName(), existingCategory.getCategoryName());
    }

//    @Test
//    void testUpdateMainCategory_CategoryNotFound() {
//        long categoryId = 1L;
//        MainCategoryDto categoryDto = new MainCategoryDto();
//        categoryDto.setCategoryName("Updated Category");
//
//        when(mainCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());
//
//        assertThrows(NoSuchElementException.class, () -> categoryService.updateMainCategory(categoryId, categoryDto));
//    }

    @Test
    void testDeleteMainCategory_Success() {
        long categoryId = 1L;
        MainCategory existingCategory = new MainCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Main Category 1");

        when(mainCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.deleteMainCategoryById(categoryId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    // Sub Category Test Cases
    @Test
    void testAddSubCategory_Success() {

        MainCategory mainCategory=new MainCategory(1L,"Main Category 1");

        SubCategoryDto categoryRequest = new SubCategoryDto();
        categoryRequest.setCategoryName("Test Sub Subcategory 1");
        categoryRequest.setMainCategory(mainCategory);

//        SubCategory saveCategory = new SubCategory();
//        saveCategory.setCategoryId(1L);
//        saveCategory.setCategoryName(categoryRequest.getCategoryName());
//        saveCategory.setMainCategory(categoryRequest.getMainCategory());

        SubCategory saveCategory = CategoryMapper.mapToSubCategory(categoryRequest);
        saveCategory.setCategoryId(1L);

//        SubCategoryDto expectedSubCategoryDto = new SubCategoryDto();
//        expectedSubCategoryDto.setCategoryId(saveCategory.getCategoryId());
//        expectedSubCategoryDto.setCategoryName(saveCategory.getCategoryName());
//        expectedSubCategoryDto.setMainCategory(saveCategory.getMainCategory());

        SubCategoryDto expectedSubCategoryDto = CategoryMapper.mapToSubCategoryDto(saveCategory);

        when(sequenceGeneratorService.generateSequence(SubCategory.SEQUENCE_NAME)).thenReturn(1L);
        when(subCategoryRepository.save(any(SubCategory.class))).thenReturn(saveCategory);

        ApiResponse response = categoryService.addSubCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(expectedSubCategoryDto, response.getData());
    }

    @Test
    void testGetSubCategoryById() {
        long categoryId = 1L;
        SubCategory subCategory = new SubCategory();
        subCategory.setCategoryId(categoryId);
        subCategory.setCategoryName("Test Sub Category");

        when(subCategoryRepository.findById(categoryId)).thenReturn(Optional.of(subCategory));

        SubCategoryDto result = categoryService.getSubCategoryById(categoryId);
        assertNotNull(result);
        assertEquals(result.getCategoryId(), subCategory.getCategoryId());
    }

    @Test
    void testGetAllSubCategories_EmptyList() {
        List<SubCategory> categories = new ArrayList<>();
        when(subCategoryRepository.findAll()).thenReturn(categories);
        List<SubCategoryDto> actualCategoryDtos = categoryService.getAllSubCategories();
        assertTrue(actualCategoryDtos.isEmpty());
    }

    @Test
    void testGetAllSubCategories() {
        List<SubCategory> categories = new ArrayList<>();
        MainCategory mainCategory1=new MainCategory(1L,"Main Category 1");
        categories.add(new SubCategory(1L, "Sub Category 1", mainCategory1));
        MainCategory mainCategory2=new MainCategory(1L,"Main Category 2");
        categories.add(new SubCategory(2L, "Sub Category 2", mainCategory2));

        when(subCategoryRepository.findAll()).thenReturn(categories);

        List<SubCategoryDto> expectedCategoryDtos = new ArrayList<>();
        for (SubCategory category : categories) {
//            SubCategoryDto categoryDto = new SubCategoryDto();
//            categoryDto.setCategoryId(category.getCategoryId());
//            categoryDto.setCategoryName(category.getCategoryName());
//            categoryDto.setMainCategory(category.getMainCategory());
            SubCategoryDto categoryDto=CategoryMapper.mapToSubCategoryDto(category);
            expectedCategoryDtos.add(categoryDto);
        }

        List<SubCategoryDto> actualCategoryDtos = categoryService.getAllSubCategories();

        assertEquals(expectedCategoryDtos, actualCategoryDtos);
    }

    @Test
    void testUpdateSubCategory_Success() {
        long categoryId = 1L;
        SubCategory existingCategory = new SubCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Sub Category");

        SubCategoryDto categoryDto = new SubCategoryDto();
        categoryDto.setCategoryName("Updated Sub Category");

        when(subCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.updateSubCategory(categoryId, categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryDto.getCategoryName(), existingCategory.getCategoryName());
    }



    @Test
    void testDeleteSubCategory_Success() {
        long categoryId = 1L;
        SubCategory existingCategory = new SubCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Sub Category 1");

        when(subCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.deleteSubCategoryById(categoryId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    // Secondary Category Test Cases
    @Test
    void testAddSecondaryCategory_Success() {
        MainCategory mainCategory=new MainCategory(1L,"Main Category 1");
        SubCategory subCategory=new SubCategory(1L,"Sub Category 1",mainCategory);

        SecondaryCategoryDto categoryRequest = new SecondaryCategoryDto();
        categoryRequest.setCategoryName("Test Secondary Subcategory 1");
        categoryRequest.setSubCategory(subCategory);

        SecondaryCategory saveCategory = new SecondaryCategory();
        saveCategory.setCategoryId(1L);
        saveCategory.setCategoryName(categoryRequest.getCategoryName());
        saveCategory.setSubCategory(categoryRequest.getSubCategory());

//        SecondaryCategory saveCategory = CategoryMapper.mapToSecondaryCategory(categoryRequest);
//        saveCategory.setCategoryId(1L);

        SecondaryCategoryDto expectedSubCategoryDto = new SecondaryCategoryDto();
        expectedSubCategoryDto.setCategoryId(saveCategory.getCategoryId());
        expectedSubCategoryDto.setCategoryName(saveCategory.getCategoryName());
        expectedSubCategoryDto.setSubCategory(saveCategory.getSubCategory());

//        SecondaryCategoryDto expectedSubCategoryDto = CategoryMapper.mapToSecondaryCategoryDto(saveCategory);

        when(sequenceGeneratorService.generateSequence(SecondaryCategory.SEQUENCE_NAME)).thenReturn(1L);
        when(secondaryCategoryRepository.save(any(SecondaryCategory.class))).thenReturn(saveCategory);

        ApiResponse response = categoryService.addSecondaryCategory(categoryRequest);

        System.out.println("expectedSubCategoryDto" + expectedSubCategoryDto);
        System.out.println("response" + response.getData());
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(expectedSubCategoryDto, response.getData());
    }

    @Test
    void testGetSecondaryCategoryById() {
        long categoryId = 1L;
        SecondaryCategory secondaryCategory=new SecondaryCategory();
        secondaryCategory.setCategoryId(categoryId);
        secondaryCategory.setCategoryName("Test Secondary Category");

        when(secondaryCategoryRepository.findById(categoryId)).thenReturn(Optional.of(secondaryCategory));

        SecondaryCategoryDto result = categoryService.getSecondaryCategoryById(categoryId);
        assertNotNull(result);
        assertEquals(result.getCategoryId(), secondaryCategory.getCategoryId());
    }

    @Test
    void testGetAllSecondaryCategories_EmptyList() {
        List<SecondaryCategory> categories = new ArrayList<>();
        when(secondaryCategoryRepository.findAll()).thenReturn(categories);
        List<SecondaryCategoryDto> actualCategoryDtos = categoryService.getAllSecondaryCategories();
        assertTrue(actualCategoryDtos.isEmpty());
    }

    @Test
    void testGetAllSecondaryCategories() {
        List<SecondaryCategory> categories = new ArrayList<>();
        MainCategory mainCategory1=new MainCategory(1L,"Main Category 1");
        SubCategory subCategory1=new SubCategory(1L,"Sub Category 1",mainCategory1);
        categories.add(new SecondaryCategory(1L, "Secondary Category 1", subCategory1));

        MainCategory mainCategory2=new MainCategory(1L,"Main Category 2");
        SubCategory subCategory2=new SubCategory(1L,"Sub Category 1",mainCategory2);
        categories.add(new SecondaryCategory(2L, "Secondary Category 2", subCategory2));

        when(secondaryCategoryRepository.findAll()).thenReturn(categories);

        List<SecondaryCategoryDto> expectedCategoryDtos = new ArrayList<>();
        for (SecondaryCategory category : categories) {
//            SubCategoryDto categoryDto = new SubCategoryDto();
//            categoryDto.setCategoryId(category.getCategoryId());
//            categoryDto.setCategoryName(category.getCategoryName());
//            categoryDto.setMainCategory(category.getMainCategory());
            SecondaryCategoryDto categoryDto=CategoryMapper.mapToSecondaryCategoryDto(category);
            expectedCategoryDtos.add(categoryDto);
        }

        List<SecondaryCategoryDto> actualCategoryDtos = categoryService.getAllSecondaryCategories();

        assertEquals(expectedCategoryDtos, actualCategoryDtos);
    }

    @Test
    void testUpdateSecondaryCategory_Success() {
        long categoryId = 1L;
        SecondaryCategory existingCategory = new SecondaryCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Secondary Category");

        SecondaryCategoryDto categoryDto = new SecondaryCategoryDto();
        categoryDto.setCategoryName("Updated Secondary Category");

        when(secondaryCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.updateSecondaryCategory(categoryId, categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryDto.getCategoryName(), existingCategory.getCategoryName());
    }

    @Test
    void testDeleteSecondaryCategory_Success() {
        long categoryId = 1L;
        SecondaryCategory existingCategory = new SecondaryCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setCategoryName("Test Secondary Category 1");

        when(secondaryCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        ApiResponse response = categoryService.deleteSecondaryCategoryById(categoryId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testGetSubCategoriesByMainCategory_Success() {
        MainCategoryDto requestCategory = new MainCategoryDto();
        requestCategory.setCategoryName("Main Category 1");

        MainCategory mainCategory = new MainCategory();
        mainCategory.setCategoryId(1L);
        mainCategory.setCategoryName(requestCategory.getCategoryName());

        List<SubCategory> categoryList = new ArrayList<>();
        categoryList.add(new SubCategory(1L, "Subcategory 1", mainCategory));
        categoryList.add(new SubCategory(2L, "Subcategory 2", mainCategory));

//        when(CategoryMapper.mapToMainCategory(requestCategory)).thenReturn(mainCategory);
        when(subCategoryRepository.findByMainCategory(any(MainCategory.class))).thenReturn(categoryList);

        List<SubCategoryDto> expectedSubCategoryDtos = new ArrayList<>();
        for (SubCategory subCategory : categoryList) {
            SubCategoryDto subCategoryDto = new SubCategoryDto();
            subCategoryDto.setCategoryId(subCategory.getCategoryId());
            subCategoryDto.setCategoryName(subCategory.getCategoryName());
            subCategoryDto.setMainCategory(subCategory.getMainCategory());
            expectedSubCategoryDtos.add(subCategoryDto);
        }

        ApiResponse response = categoryService.getSubCategoriesByMainCategory(requestCategory);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(expectedSubCategoryDtos, response.getData());
//        assertEquals(2,response.getData().equals(2));
    }

    @Test
    void testGetSubCategoriesByMainCategory_EmptyList() {
        MainCategoryDto requestCategory = new MainCategoryDto();
        requestCategory.setCategoryName("Main Category 1");

        MainCategory category = new MainCategory();
        category.setCategoryId(1L);
        category.setCategoryName(requestCategory.getCategoryName());

        List<SubCategory> categoryList = new ArrayList<>();

//        when(CategoryMapper.mapToMainCategory(requestCategory)).thenReturn(category);
        when(subCategoryRepository.findByMainCategory(any(MainCategory.class))).thenReturn(categoryList);

        ApiResponse response = categoryService.getSubCategoriesByMainCategory(requestCategory);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
//        assertTrue(response.getData().isEmpty());
    }


    @Test
    void testGetSecondaryCategoriesBySubCategory_Success() {
        Long subCategoryId = 1L;

        SubCategoryDto requestCategory = new SubCategoryDto();
        requestCategory.setCategoryId(subCategoryId);
        requestCategory.setCategoryName("Sub Category");

        SubCategory category = new SubCategory();
        category.setCategoryId(subCategoryId);
        category.setCategoryName(requestCategory.getCategoryName());

        List<SecondaryCategory> categoryList = new ArrayList<>();
        categoryList.add(new SecondaryCategory(1L, "Secondary Category 1", category));
        categoryList.add(new SecondaryCategory(2L, "Secondary Category 2", category));

//        when(CategoryMapper.mapToSubCategory(requestCategory)).thenReturn(category);
        when(secondaryCategoryRepository.findBySubCategory(any(SubCategory.class))).thenReturn(categoryList);

        List<SecondaryCategoryDto> expectedSecondaryCategoryDtos = new ArrayList<>();
        for (SecondaryCategory secondaryCategory : categoryList) {
            SecondaryCategoryDto secondaryCategoryDto = new SecondaryCategoryDto();
            secondaryCategoryDto.setCategoryId(secondaryCategory.getCategoryId());
            secondaryCategoryDto.setCategoryName(secondaryCategory.getCategoryName());
            secondaryCategoryDto.setSubCategory(secondaryCategory.getSubCategory());
            expectedSecondaryCategoryDtos.add(secondaryCategoryDto);
        }

//        List<SecondaryCategoryDto> response = categoryService.getSecondaryCategoriesBySubCategorys(subCategoryId);
       ApiResponse response = categoryService.getSecondaryCategoriesBySubCategory(subCategoryId);
        assertNotNull(response);
//        assertEquals(response, categoryList);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(expectedSecondaryCategoryDtos, response.getData());
        System.out.println(response.getData());
    }

    @Test
    void testGetSecondaryCategoriesBySubCategory_EmptyList() {
        Long subCategoryId = 1L;

        SubCategoryDto requestCategory = new SubCategoryDto();
        requestCategory.setCategoryId(subCategoryId);

        SubCategory category = new SubCategory();
        category.setCategoryId(subCategoryId);

        List<SecondaryCategory> categoryList = new ArrayList<>();

//        when(CategoryMapper.mapToSubCategory(requestCategory)).thenReturn(category);
        when(secondaryCategoryRepository.findBySubCategory(any(SubCategory.class))).thenReturn(categoryList);

        ApiResponse response = categoryService.getSecondaryCategoriesBySubCategory(subCategoryId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }


}