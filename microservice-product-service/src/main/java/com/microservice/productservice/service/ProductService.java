package com.microservice.productservice.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.InventoryDto;
import com.microservice.productservice.dto.ProductDto;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.dto.UserDto;
import com.microservice.productservice.mapper.ProductMapper;
import com.microservice.productservice.model.Category;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.CategoryRepository;
import com.microservice.productservice.repository.ProductRepository;
import com.microservice.productservice.utils.ConstantMethods;
import com.microservice.productservice.utils.SequenceGenaratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private MessageSource messageSource;

	public UserDto getUserDetailByUserId(long userId) {
		return restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + userId, UserDto.class);
	}

	public InventoryDto ProductInventoryDetails(long productId) {
		return restTemplate.getForObject("http://inventory-service/api/inventory/product-inventory/" + productId,
				InventoryDto.class);
	}

	public List<InventoryDto> inventoryDetails() {
		InventoryDto[] inventoryData = restTemplate.getForObject("http://inventory-service/api/inventory/get-inventory",
				InventoryDto[].class);
		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();

		for (InventoryDto inventoryDto : inventoryData) {
			inventoryList.add(inventoryDto);
		}
		return inventoryList;
	}

	public ApiResponse createProduct(ProductRequest productRequest) throws IOException {
		Product newProduct = new Product();
		productRequest.setProductId(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME));
		newProduct.setProductId(productRequest.getProductId());
		log.info("Product {} is saved", newProduct.getProductId());
		newProduct.setProductName(productRequest.getProductName());
		newProduct.setDescription(productRequest.getDescription());
		newProduct.setPrice(productRequest.getPrice());
		newProduct.setCategory(productRequest.getCategory());
		newProduct.setProductStatus("CREATED");
		MultipartFile image = productRequest.getImage();

		newProduct.setCreatedAt(Instant.now());

		byte[] imageBytes = image.getBytes();
		newProduct.setImage(imageBytes);

		newProduct.setProductInventoryStatus(true);
		Product savedProduct=productRepository.save(newProduct);

		InventoryDto inventoryDto = new InventoryDto();
//		ProductDto productDto = getProductById(newProduct.getProductId());
//		System.out.println("ProductDto : " + productDto);
		inventoryDto.setProductId(savedProduct.getProductId());
		inventoryDto.setQuantityInStock(productRequest.getQuantityInStock());
		System.out.println("Inventory data : " + inventoryDto);

		restTemplate.postForObject("http://inventory-service/api/inventory/add-inventory", inventoryDto,
				InventoryDto.class);

		return ConstantMethods.successResponse(savedProduct,
				messageSource.getMessage("api.response.product.added", null, Locale.ENGLISH), HttpStatus.CREATED);
	}

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDto = new ArrayList<>();

		//List<InventoryDto> inventoryDtos = inventoryDetails();

		for (Product product : products) {
			if (!product.getProductStatus().equals("DELETED")) {

				ProductDto dto = ProductMapper.mapToProductDto(product);
				productDto.add(dto);
			}

		}
//		for (ProductDto product : productDto) {
//			for (InventoryDto inventory : inventoryDtos) {
//				if (product.getProductId() == inventory.getProductId()) {
//					 product.setQuantityInStock(inventory.getQuantityInStock());
//				}
//			}
//		}
		return productDto;
	}

	public ProductDto getProductById(long id) {
		Product productDetails = productRepository.findById(id).get();
		// UserDto userDto = getUserDetailByUserId(productDetails.getCreatedByUserId());
//		InventoryDto inventory = ProductInventoryDetails(productDetails.getProductId());
		ProductDto productDto = ProductMapper.mapToProductDto(productDetails);
//		productDto.setQuantityInStock(inventory.getQuantityInStock());
//		for (InventoryDto inventory : inventoryDtos) {
//
//			if (productDetails.getProductId() == inventory.getProduct().getProductId()) {
//				productDto.setQuantityInStock(inventory.getQuantityInStock());
//			}
//		}

		// ProductDto productDto = ProductMapper.mapToProductDto(productDetails);
		// productDto.setCreatedByUser(userDto);
		return productDto;
	}

	public ProductDto getProductDetailsById(long productId) {
		Product productDetails = productRepository.findById(productId).get();
		ProductDto productResponse = new ProductDto();
		productResponse.setProductId(productDetails.getProductId());
		productResponse.setProductName(productDetails.getProductName());
		productResponse.setDescription(productDetails.getDescription());
		productResponse.setImage(productDetails.getImage());
		productResponse.setCategory(productDetails.getCategory());
		productResponse.setPrice(productDetails.getPrice());
		return productResponse;
		// return ProductMapper.mapToProductDto(productDetails);
	}

	public ApiResponse deleteProduct(long id) {
		Product product = productRepository.findById(id).get();
		product.setUpdatedAt(Instant.now());
		product.setProductStatus("DELETED");
		productRepository.save(product);
		return ConstantMethods.successResponse(product,
				messageSource.getMessage("api.response.product.delete", null, Locale.ENGLISH), HttpStatus.OK);
	}

	public Product update(long id, String productName, String description, Double price, MultipartFile image)
			throws IOException {
		Product product1 = productRepository.findById(id).get();
		// byte[] imageBytes = image.getBytes();
		// if (productName != null)
		product1.setProductName(productName);
		product1.setDescription(description);
		if (price != 0)
			product1.setPrice(price);
		if (image != null && !image.isEmpty()) {
			byte[] imageBytes = image.getBytes();
			product1.setImage(imageBytes);
		}
//		product1.setProductStatus("CREATED");
		product1.setUpdatedAt(Instant.now());
		return productRepository.save(product1);

	}

	public List<ProductDto> getProductsByCategoryName(Category category) {

		// Category category1=categoryRepository.findById(category).get();

		List<Product> products = productRepository.findByCategory(category);

		List<ProductDto> productDto = new ArrayList<>();
		// if (products != category) {

		for (Product product : products) {
			if (!product.getProductStatus().equals("DELETED")) {

				ProductDto dto = ProductMapper.mapToProductDto(product);
				productDto.add(dto);
			}
//			ProductDto dto = ProductMapper.mapToProductDto(product);
//			productDto.add(dto);
		}
		return productDto;
		/*
		 * } else { throw new RecordNotFoundException("product not found..."); }
		 */
	}
}
