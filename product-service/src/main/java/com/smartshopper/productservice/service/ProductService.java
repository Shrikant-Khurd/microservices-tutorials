package com.smartshopper.productservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartshopper.productservice.dto.ApiResponse;
import com.smartshopper.productservice.dto.InventoryDto;
import com.smartshopper.productservice.dto.MainCategoryDto;
import com.smartshopper.productservice.dto.ProductDto;
import com.smartshopper.productservice.dto.ProductRequest;
import com.smartshopper.productservice.feign.inventoryservice.clients.InventoryFeignClient;
import com.smartshopper.productservice.mapper.CategoryMapper;
import com.smartshopper.productservice.mapper.ProductMapper;
import com.smartshopper.productservice.model.MainCategory;
import com.smartshopper.productservice.model.Product;
import com.smartshopper.productservice.model.SecondaryCategory;
import com.smartshopper.productservice.model.SubCategory;
import com.smartshopper.productservice.repository.ProductRepository;
import com.smartshopper.productservice.utils.ConstantMethods;
import com.smartshopper.productservice.utils.SequenceGeneratorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	private ProductRepository productRepository;

	private InventoryFeignClient inventoryFeignClient;
//	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	private MessageSource messageSource;

	@Autowired
	public ProductService(ProductRepository productRepository, InventoryFeignClient inventoryFeignClient,
						  SequenceGeneratorService sequenceGeneratorService,MessageSource messageSource) {
		super();
		this.productRepository = productRepository;
		this.inventoryFeignClient = inventoryFeignClient;
		this.messageSource = messageSource;
		this.sequenceGeneratorService=sequenceGeneratorService;
	}

	public ApiResponse createProduct(ProductRequest productRequest) throws IOException {
		Product newProduct = new Product();
		productRequest.setProductId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
		newProduct.setProductId(productRequest.getProductId());
		log.info("Product {} is saved", newProduct.getProductId());
		newProduct.setProductName(productRequest.getProductName());
		newProduct.setDescription(productRequest.getDescription());
		newProduct.setPrice(productRequest.getPrice());
		newProduct.setMainCategory(productRequest.getMainCategory());
		newProduct.setSubCategory(productRequest.getSubCategory());
		newProduct.setSecondaryCategory(productRequest.getSecondaryCategory());
		newProduct.setSizes(productRequest.getSizes());
		newProduct.setProductStatus("CREATED");
		MultipartFile image = productRequest.getImage();

		newProduct.setCreatedAt(LocalDateTime.now());

		byte[] imageBytes = image.getBytes();
		newProduct.setImage(imageBytes);

		newProduct.setProductInventoryStatus(true);
		Product savedProduct = productRepository.save(newProduct);

		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setProductId(savedProduct.getProductId());
		inventoryDto.setQuantityInStock(productRequest.getQuantityInStock());

		inventoryFeignClient.addProductInInventory(inventoryDto);

		return ConstantMethods.successResponse(savedProduct,
				messageSource.getMessage("api.response.product.added", null, Locale.ENGLISH), HttpStatus.CREATED);
	}

	public ApiResponse getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDto = new ArrayList<>();
		for (Product product : products) {
			if (!product.getProductStatus().equals("DELETED")) {
				ProductDto dto = ProductMapper.mapToProductDto(product);
				productDto.add(dto);
			}
		}
		return ConstantMethods.successResponse(productDto,
				messageSource.getMessage("api.response.product.getlist", null, Locale.ENGLISH), HttpStatus.OK);
	}

	public ProductDto getProductDetailsById(long productId) {
		Product productDetails = productRepository.findById(productId).get();
		return ProductMapper.mapToProductDto(productDetails);
	}

	public ApiResponse deleteProduct(long id) {
		Product product = productRepository.findById(id).get();
		product.setUpdatedAt(LocalDateTime.now());
		product.setProductStatus("DELETED");
		productRepository.save(product);
		return ConstantMethods.successResponse(product,
				messageSource.getMessage("api.response.product.delete", null, Locale.ENGLISH), HttpStatus.OK);
	}

	public ApiResponse updateProductDetail(long id, String productName, String description, Double price,
			MainCategory mainCategory, SubCategory subCategory, SecondaryCategory secondaryCategory, Set<String> sizes,
			MultipartFile image) throws IOException {
		Product product = productRepository.findById(id).get();
		product.setProductName(productName);
		product.setDescription(description);
		product.setMainCategory(mainCategory);
		product.setSubCategory(subCategory);
//		product.setSecondaryCategory(secondaryCategory);
	
		if ( secondaryCategory != null  )//|| product.getSecondaryCategory()== null)
			product.setSecondaryCategory(secondaryCategory);

		if (!sizes.isEmpty())
			product.setSizes(sizes);
		if (price != 0)
			product.setPrice(price);
		if (image != null && !image.isEmpty()) {
			byte[] imageBytes = image.getBytes();
			product.setImage(imageBytes);
		}
		product.setUpdatedAt(LocalDateTime.now());
		productRepository.save(product);

		return ConstantMethods.successResponse(product,
				messageSource.getMessage("api.response.product.update.successfully", null, Locale.ENGLISH),
				HttpStatus.OK);

	}

	public List<ProductDto> getProductsByCategoryName(long mainCategoryId) {
		MainCategoryDto requestCategory = new MainCategoryDto();
		requestCategory.setCategoryId(mainCategoryId);
		MainCategory category = CategoryMapper.mapToMainCategory(requestCategory);
		List<Product> products = productRepository.findByMainCategory(category);
		List<ProductDto> productDto = new ArrayList<>();
		for (Product product : products) {
			if (!product.getProductStatus().equals("DELETED")) {
				ProductDto dto = ProductMapper.mapToProductDto(product);
				productDto.add(dto);
			}
		}
		return productDto;
	}

	public ApiResponse updateProduct(long id, ProductRequest productRequest) throws IOException {
		Product product = productRepository.findById(id).get();
		product.setProductName(productRequest.getProductName());
		product.setDescription(productRequest.getDescription());
		product.setMainCategory(productRequest.getMainCategory());
		product.setSubCategory(productRequest.getSubCategory());

		if (!productRequest.getSizes().isEmpty())
			product.setSizes(productRequest.getSizes());
		if (productRequest.getPrice() != 0)
			product.setPrice(productRequest.getPrice());
		if (productRequest.getImage() != null && !productRequest.getImage().isEmpty()) {
			byte[] imageBytes = productRequest.getImage().getBytes();
			product.setImage(imageBytes);
		}
		product.setUpdatedAt(LocalDateTime.now());
		productRepository.save(product);

		return ConstantMethods.successResponse(product,
				messageSource.getMessage("api.response.product.update.successfully", null, Locale.ENGLISH),
				HttpStatus.OK);
	}
}
