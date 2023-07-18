package com.microservice.productservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.ProductDto;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.model.Category;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
	private final ProductService productService;

	@PostMapping("/add-product")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ApiResponse> createProduct(@ModelAttribute ProductRequest productRequest) throws IOException {
		ApiResponse productResponse = productService.createProduct(productRequest);
		return new ResponseEntity<>(productResponse, productResponse.getHttpStatus());
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

	@PostMapping("/update/{id}")
	public ResponseEntity<String> update(@PathVariable long id, @RequestParam("productName") String productName,
			@RequestParam("description") String description, @RequestParam("price") Double price,
			@RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
		productService.update(id, productName, description, price, image);
		return new ResponseEntity<String>("Product updated successfully", HttpStatus.OK);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {
		ApiResponse response = productService.deleteProduct(id);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@GetMapping("/category")
	public ResponseEntity<List<ProductDto>> getProductsByCategoryName(@RequestParam("category") Category category) {
		List<ProductDto> product = productService.getProductsByCategoryName(category);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
}
