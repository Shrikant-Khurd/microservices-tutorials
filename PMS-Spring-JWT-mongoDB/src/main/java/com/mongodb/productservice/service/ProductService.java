package com.mongodb.productservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.productservice.dto.ProductDto;
import com.mongodb.productservice.dto.ProductRequest;
import com.mongodb.productservice.mapper.ProductMapper;
import com.mongodb.productservice.model.Category;
import com.mongodb.productservice.model.Product;
import com.mongodb.productservice.repository.ProductRepository;
import com.mongodb.productservice.utils.SequenceGenaratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	
	private final String FOLDER_PATH= "E:/Java-Workspace-Spring-tool-suite/PMS-Spring-JWT-mongoDB/images/";

	public Product createProduct(ProductRequest productRequest) throws IOException {
		Product newProduct = new Product();
		newProduct.setProductId(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME));
		log.info("Product {} is saved", newProduct.getProductId());
		newProduct.setProductId(productRequest.getProductId());
		newProduct.setName(productRequest.getProductName());
		newProduct.setDescription(productRequest.getDescription());
		newProduct.setPrice(productRequest.getPrice());
		newProduct.setCategory(productRequest.getCategory());
		newProduct.setProductStatus("CREATED");
		MultipartFile image = productRequest.getImage();

		newProduct.setCreatedAt(Instant.now());

		byte[] imageBytes = image.getBytes();
		newProduct.setImage(imageBytes);

		
		
		return productRepository.save(newProduct);
	}
	public Product createProductStoredInFile(ProductRequest productRequest) throws IOException {
		Product newProduct = new Product();
		String filePath= FOLDER_PATH+productRequest.getImage().getOriginalFilename();
		
		productRequest.setProductId(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME));
		log.info("Product {} is saved", newProduct.getProductId());
		newProduct.setProductId(productRequest.getProductId());
		newProduct.setName(productRequest.getProductName());
		newProduct.setDescription(productRequest.getDescription());
		newProduct.setPrice(productRequest.getPrice());
		newProduct.setCategory(productRequest.getCategory());
		newProduct.setProductStatus("CREATED");
		
		newProduct.setCreatedAt(Instant.now());

        String imageName = productRequest.getImage().getOriginalFilename();
        Path imagePath = Path.of(FOLDER_PATH, imageName);
        Files.copy(productRequest.getImage().getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        newProduct.setImagePath(imagePath.toString());

		
		return productRepository.save(newProduct);
	}

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDto = new ArrayList<>();
		for (Product product : products) {
			if (!product.getProductStatus().equals("DELETED")) {

				ProductDto dto = ProductMapper.mapToProductDto(product);
				productDto.add(dto);
			}

		}
		return productDto;
	}

	public ProductDto getProductById(long id) {
		Product productDetails = productRepository.findById(id).get();
		ProductDto productDto = ProductMapper.mapToProductDto(productDetails);

		return productDto;
	}

	public void deleteProduct(long id) {
		Product product = productRepository.findById(id).get();
		product.setUpdatedAt(Instant.now());
		product.setProductStatus("DELETED");
		productRepository.save(product);
	}

	public Product update(long id, String productName, String description, Double price, MultipartFile image)
			throws IOException {
		Product product1 = productRepository.findById(id).get();
		// byte[] imageBytes = image.getBytes();
		// if (productName != null)
		product1.setName(productName);
		product1.setDescription(description);
		if (price != 0)
			product1.setPrice(price);
		if (image != null && !image.isEmpty()) {
			byte[] imageBytes = image.getBytes();
			product1.setImage(imageBytes);
		}

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
		}
		return productDto;
		/*
		 * } else { throw new RecordNotFoundException("product not found..."); }
		 */
	}
}
