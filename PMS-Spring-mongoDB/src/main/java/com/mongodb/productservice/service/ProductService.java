package com.mongodb.productservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.productservice.dto.ProductDto;
import com.mongodb.productservice.mapper.ProductMapper;
import com.mongodb.productservice.model.Product;
import com.mongodb.productservice.model.ProductMongo;
import com.mongodb.productservice.repository.ProductMongoRepository;
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
	@Value("${image.upload.directory}") // Configure the directory path in application.properties
	private String imageUploadDirectory;

	private final ProductMongoRepository productMongoRepository;

	public ProductMongo createProducts(ProductMongo productMongoDB) {
		// productMongoDB.setId(sequenceGenaratorService.generateSequence(ProductMongo.SEQUENCE_NAME));
		// long id= Long.parseLong(productMongoDB.getProductId());
		// productMongoDB.setProductId(productMongoDB.getId());
		return productMongoRepository.save(productMongoDB);
	}

	public List<String> uploadImages(List<MultipartFile> images) throws IOException {
		List<String> imageUrls = new ArrayList<>();
		for (MultipartFile image : images) {
			// Generate a unique file name
			String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

			// Upload the image to a storage location (e.g., local file system, cloud
			// storage)
			// Here, we assume the images are stored in a "images" directory in the project
			// root folder
			Path filePath = Paths.get("images/" + fileName);
			Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// Create the image URL
			String imageUrl = filePath.toString();
			imageUrls.add(imageUrl);
		}
		return imageUrls;
	}

	public List<ProductMongo> getAllProduct() {
		return productMongoRepository.findAll();
	}

	public Product createProduct(Product productRequest) {
		productRequest.setId(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME));
		log.info("Product {} is saved", productRequest.getId());
		return productRepository.save(productRequest);
	}

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDto = new ArrayList<>();
		for (Product product : products) {

			ProductDto dto = ProductMapper.mapToProductDto(product);

			productDto.add(dto);
		}
		return productDto;
	}

	public ProductDto getProductById(long id) {
		Product productDetails = productRepository.findById(id).get();

		ProductDto productDto = ProductMapper.mapToProductDto(productDetails);

		return productDto;
	}

	public void deleteProduct(long id) {
		productRepository.deleteById(id);
	}

	public void productAdd(String productName, double price, String imageUrl) throws IOException {
		ProductMongo product = new ProductMongo();
		product.setProductName(productName);
		product.setPrice(price);
		product.setProductId(sequenceGenaratorService.generateSequence(ProductMongo.SEQUENCE_NAME));
		// product.setImages(imageUrl);

		productMongoRepository.save(product);
	}

	public String saveImage(MultipartFile image) throws IOException {
		// Generate a unique filename for the image
		String fileName = image.getOriginalFilename();
		Path filePath = Path.of(imageUploadDirectory, fileName);

		// Save the image file to the directory
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Return the image URL
		return filePath.toUri().toString();
	}

	public void addPro(String productName, Double price, byte[] imageBytes) {
//		public void addPro(String productName, Double price, String imageBytes) {
		// byte[] imageBytes = Base64.getDecoder().decode(base64Image);
		ProductMongo product = new ProductMongo();
		product.setProductId(sequenceGenaratorService.generateSequence(ProductMongo.SEQUENCE_NAME));
		product.setProductName(productName);
		product.setPrice(price);
		product.setImage(imageBytes);
		productMongoRepository.save(product);
	}

	public ProductMongo getProductByProductId(long id) {
		return productMongoRepository.findById(id).orElse(null);
	}

	public ProductMongo updateProduct(long id, ProductDto product) throws IOException {
		ProductMongo product1 = productMongoRepository.findById(id).get();
	
		MultipartFile image = product.getImage();
		if (product.getProductName() != null)
			product1.setProductName(product.getProductName());
		if (product.getPrice() != 0)
			product1.setPrice(product.getPrice());
//		if (product.getImage() != null && !product.getImage().isEmpty()) {
//			byte[] imageBytes = product.getImage().getBytes();
//			product1.setImage(imageBytes);
//		}
		if (image != null && !image.isEmpty()) {
			byte[] imageBytes = image.getBytes();
			product1.setImage(imageBytes);
		}
		return productMongoRepository.save(product1);

	}

	public ProductMongo update(long id, String productName, Double price, MultipartFile image) throws IOException {
		ProductMongo product1 = productMongoRepository.findById(id).get();
		// byte[] imageBytes = image.getBytes();
		// if (productName != null)
		product1.setProductName(productName);
		if (price != 0)
			product1.setPrice(price);
		if (image != null && !image.isEmpty()) {
			byte[] imageBytes = image.getBytes();
			product1.setImage(imageBytes);
		}
		// }
		return productMongoRepository.save(product1);

	}
}
