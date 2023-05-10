package com.microservice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.ProductRepository;
import com.microservice.productservice.utils.SequenceGenaratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	
	public Product createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.id(sequenceGenaratorService.generateSequence(Product.SEQUENCE_NAME))
				.name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();

		log.info("Product {} is saved", product.getId());
		return productRepository.save(product);
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();

		return products.stream().map(this::mapToProductResponse).toList();
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.productId(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).build();
	}

	public ProductResponse getProductById(long id) {
		// TODO Auto-generated method stub
		Product product = productRepository.findById(id).get();
		return mapToProductResponse(product);
	}

	public void deleteProduct(long id) {
		productRepository.deleteById(id);
	}
}
