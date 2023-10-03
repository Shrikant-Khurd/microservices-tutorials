package com.microservice.productservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.MainCategoryDto;
import com.microservice.productservice.dto.ProductDto;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.model.MainCategory;
import com.microservice.productservice.model.SecondaryCategory;
import com.microservice.productservice.model.SubCategory;
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

	@GetMapping("/get-all-products")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ApiResponse> getAllProducts() {
		ApiResponse productResponses = productService.getAllProducts();
		return new ResponseEntity<>(productResponses, productResponses.getHttpStatus());
	}

	@GetMapping("/getProductDetailsById/{productId}")
	public ResponseEntity<ProductDto> getProductDetailsById(@PathVariable("productId") long productId) {
		ProductDto product = productService.getProductDetailsById(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<ApiResponse> updateProductDetail(@PathVariable long id,
			@RequestParam("productName") String productName, @RequestParam("description") String description,
			@RequestParam("price") Double price, @RequestParam("mainCategory") MainCategory mainCategory,
			@RequestParam("subCategory") SubCategory subCategory,
			@RequestParam("secondaryCategory") SecondaryCategory secondaryCategory,
			@RequestParam("sizes") Set<String> sizes,
			@RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
	
		ApiResponse response = productService.updateProductDetail(id, productName, description, price, mainCategory,
				subCategory, secondaryCategory, sizes, image);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@PostMapping("/update-product/{id}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable long id,
			@ModelAttribute ProductRequest productRequest) throws IOException {
		ApiResponse response = productService.updateProduct(id, productRequest);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {
		ApiResponse response = productService.deleteProduct(id);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	@GetMapping("/category")
	public ResponseEntity<List<ProductDto>> getProductsByCategoryName(@RequestParam("category") long mainCategoryId) {
		List<ProductDto> product = productService.getProductsByCategoryName(mainCategoryId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
}
