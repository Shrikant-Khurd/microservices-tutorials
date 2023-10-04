package com.smartshopper.productservice.service;

import com.smartshopper.productservice.builder.ProductBuilder;
import com.smartshopper.productservice.dto.*;
import com.smartshopper.productservice.feign.inventoryservice.clients.InventoryFeignClient;
import com.smartshopper.productservice.mapper.CategoryMapper;
import com.smartshopper.productservice.mapper.ProductMapper;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.Product;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;
import com.smartshopper.productservice.repository.ProductRepository;
import com.smartshopper.productservice.utils.SequenceGeneratorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);

    @Mock
    private InventoryFeignClient inventoryFeignClient;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
//    private final SequenceGeneratorService sequenceGeneratorService = mock(SequenceGeneratorService.class);

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProductService productService;



    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateProduct_Success() throws IOException {
//        ProductRequest productRequest = new ProductRequest();
//        productRequest.setProductName("Product 1");
//        productRequest.setDescription("Description 1");
//        productRequest.setPrice(10.0);
//        MainCategory mainCategory=new MainCategory(1L,"Main Category 1");
//        productRequest.setMainCategory(mainCategory);
//        SubCategory subCategory=new SubCategory(1L,"Sub Category 1",mainCategory);
//        productRequest.setSubCategory(subCategory);
//        SecondaryCategory secondaryCategory=new SecondaryCategory(1L, "Secondary Category 1", subCategory);
//        productRequest.setSecondaryCategory(secondaryCategory);
//        Set<String> sizes=new HashSet<>();
//        sizes.add("S");
//        sizes.add("M");
//        sizes.add("L");
//        productRequest.setSizes(sizes);
//        productRequest.setProductStatus("CREATED");
//        productRequest.setQuantityInStock(100);

        ProductRequest productRequest= ProductBuilder.productRequestBuilder();

        Product newProduct = new Product();
        newProduct.setProductId(productRequest.getProductId());
        newProduct.setProductName(productRequest.getProductName());
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setMainCategory(productRequest.getMainCategory());
        newProduct.setSubCategory(productRequest.getSubCategory());
        newProduct.setSecondaryCategory(productRequest.getSecondaryCategory());
        newProduct.setSizes(productRequest.getSizes());
        newProduct.setProductStatus(productRequest.getProductStatus());
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setImage(new byte[0]);
        newProduct.setProductInventoryStatus(true);

        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        productRequest.setImage(image);

        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setProductId(newProduct.getProductId());
        inventoryDto.setQuantityInStock(productRequest.getQuantityInStock());

        when(inventoryFeignClient.addProductInInventory(any(InventoryDto.class))).thenReturn(inventoryDto);

        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        when(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME)).thenReturn(1L);

        ApiResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(newProduct, response.getData());
    }

    @Test
    void testGetAllProducts_Success() {
        List<Product> products = new ArrayList<>();
        products.add(ProductBuilder.productBuilder());
        products.add(ProductBuilder.productBuilder());

        List<ProductDto> expectedProductDtos = new ArrayList<>();

        for (Product product : products) {
            if (!product.getProductStatus().equals("DELETED")) {
                ProductDto productDto = ProductMapper.mapToProductDto(product);
                expectedProductDtos.add(productDto);
            }
        }
        when(productRepository.findAll()).thenReturn(products);
        ApiResponse response = productService.getAllProducts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(expectedProductDtos, response.getData());
        System.out.println(response.getData());
    }

    @Test
    void testGetAllProducts_EmptyList() {
        List<Product> products = new ArrayList<>();

        when(productRepository.findAll()).thenReturn(products);
        ApiResponse response = productService.getAllProducts();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    void testGetProductDetailsById_Success() {
        long productId = 1L;

        Product product = ProductBuilder.productBuilder();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDto productDto = productService.getProductDetailsById(productId);

        assertNotNull(productDto);
        assertEquals(productId, productDto.getProductId());
        assertEquals(product.getProductName(), productDto.getProductName());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getMainCategory(), productDto.getMainCategory());
        assertEquals(product.getSubCategory(), productDto.getSubCategory());
        assertEquals(product.getSecondaryCategory(), productDto.getSecondaryCategory());
        assertEquals(product.getSizes(), productDto.getSizes());
        assertEquals(product.getProductStatus(), productDto.getProductStatus());
        assertEquals(product.getCreatedAt(), productDto.getCreatedAt());

    }

//    @Test
//    void testGetProductDetailsById_ProductNotFound() {
//        long productId = 1L;
//
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> productService.getProductDetailsById(productId));
//    }

    @Test
    void testDeleteProduct_Success() {
        long productId = 1L;

        Product product = ProductBuilder.productBuilder();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ApiResponse response = productService.deleteProduct(productId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(product, response.getData());
        assertEquals("DELETED", product.getProductStatus());
    }

    @Test
    void testUpdateProductDetail_Success() throws IOException {
        long productId = 1L;
        String productName = "Product 2";
        String description = "Description 2";
        Double price = 200.0;
        MainCategory mainCategory = new MainCategory(1L,"Main Category 1");
        SubCategory subCategory = new SubCategory(2L,"Sub Category 2", mainCategory);
        SecondaryCategory secondaryCategory = new SecondaryCategory(3L,"Secondary Category 3", subCategory);
        Set<String> sizes = new HashSet<>(Arrays.asList("S", "M", "XL"));
        byte[] imageBytes = new byte[0];
        MultipartFile image = new MockMultipartFile("image.jpg", imageBytes);

        Product product = ProductBuilder.productBuilder();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));


        ApiResponse response = productService.updateProductDetail(productId, productName, description, price, mainCategory, subCategory, secondaryCategory, sizes, image);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(product, response.getData());
        assertEquals(productName, product.getProductName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(mainCategory, product.getMainCategory());
        assertEquals(subCategory, product.getSubCategory());
        assertEquals(secondaryCategory, product.getSecondaryCategory());
        assertEquals(sizes, product.getSizes());

        System.out.println("Original Data: " +product);
        System.out.println("Updated Data: "+response.getData());
    }

    @Test
    void testGetProductsByCategoryName_Success() {
        long mainCategoryId = 1L;

        MainCategoryDto requestCategory = new MainCategoryDto();
        requestCategory.setCategoryId(mainCategoryId);
        MainCategory category = CategoryMapper.mapToMainCategory(requestCategory);

        Product product1 = ProductBuilder.productBuilder();
        Product product2 = ProductBuilder.productBuilder();
        List<Product> products = Arrays.asList(product1,product2);

        when(productRepository.findByMainCategory(any(MainCategory.class))).thenReturn(products);

        List<ProductDto> productDtos = productService.getProductsByCategoryName(mainCategoryId);
        System.out.println(productDtos);

        assertNotNull(productDtos);
        assertEquals(2, productDtos.size());
        assertEquals(product1.getProductName(), productDtos.get(0).getProductName());
        assertEquals(product2.getProductName(), productDtos.get(1).getProductName());

//        Assertions.assertThat(productService)
//                .isNotNull()
//                .hasSize(1)
//                .isEqualTo(productDtos);
    }

    @Test
    void testGetProductsByCategoryName_NoProductsFound() {
        long mainCategoryId = 1L;
        MainCategoryDto requestCategory = new MainCategoryDto();
        requestCategory.setCategoryId(mainCategoryId);
        MainCategory category = CategoryMapper.mapToMainCategory(requestCategory);

        when(productRepository.findByMainCategory(category)).thenReturn(Collections.emptyList());

        List<ProductDto> productDtos = productService.getProductsByCategoryName(mainCategoryId);
        assertNotNull(productDtos);
        assertTrue(productDtos.isEmpty());
    }
}