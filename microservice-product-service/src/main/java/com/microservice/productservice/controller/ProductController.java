package com.microservice.productservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.productservice.dto.ProductDto;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.ProductRepository;
import com.microservice.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
	private final ProductService productService;

	@PostMapping("/add-product")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Product> createProduct(@RequestBody Product productRequest) {
		Product productResponse = productService.createProduct(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@GetMapping("get-all-products")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ProductDto>> getAllProducts() {
		List<ProductDto> productResponses = productService.getAllProducts();
		return new ResponseEntity<>(productResponses, null, HttpStatus.CREATED);
	}

	@GetMapping("/get-product-byId/{id}")
	public ResponseEntity<ProductDto> getProductDetails(@PathVariable long id) {
		ProductDto product = productService.getProductById(id);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>("Delete product successfully", HttpStatus.CREATED);
	}
}
