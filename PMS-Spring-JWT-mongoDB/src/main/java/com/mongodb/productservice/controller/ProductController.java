package com.mongodb.productservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.productservice.dto.ProductDto;
import com.mongodb.productservice.dto.ProductRequest;
import com.mongodb.productservice.model.Category;
import com.mongodb.productservice.model.Product;
import com.mongodb.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
	private final ProductService productService;

	@PostMapping("/add-product")
	public ResponseEntity<Product> createProduct(@ModelAttribute ProductRequest productRequest) throws IOException {
		Product productResponse = productService.createProduct(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}
	@PostMapping("/add-product-file")
	public ResponseEntity<Product> createProductStoredInFile(@ModelAttribute ProductRequest productRequest) throws IOException {
		Product productResponse = productService.createProductStoredInFile(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@GetMapping("/get-all-products")
	public ResponseEntity<List<ProductDto>> getAllProducts() {
		List<ProductDto> productResponses = productService.getAllProducts();
		return new ResponseEntity<>(productResponses, null, HttpStatus.OK);
	}

	@GetMapping("/get-product-byId/{id}")
	public ResponseEntity<ProductDto> getProductDetails(@PathVariable long id) {
		ProductDto product = productService.getProductById(id);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>("Delete product successfully", HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable long id, @RequestParam("productName") String productName,
			@RequestParam("description") String description, @RequestParam("price") Double price,
			@RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
		productService.update(id, productName, description, price, image);
		return new ResponseEntity<String>("Product updated successfully", HttpStatus.OK);
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<ProductDto>> getProductsByCategoryName(@RequestParam("category") Category category) {
		List<ProductDto> product = productService.getProductsByCategoryName(category);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
}
