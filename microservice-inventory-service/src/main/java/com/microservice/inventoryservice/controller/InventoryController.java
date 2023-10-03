package com.microservice.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.inventoryservice.dto.ApiResponse;
import com.microservice.inventoryservice.dto.InventoryDto;
import com.microservice.inventoryservice.dto.InventoryRequestDto;
import com.microservice.inventoryservice.dto.InventoryResponse;
import com.microservice.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@PostMapping("/add-stock")
	public ResponseEntity<ApiResponse> addProductStock(@RequestBody InventoryRequestDto productRequest) {
		ApiResponse productResponse = inventoryService.addProductStock(productRequest);
		return new ResponseEntity<>(productResponse, productResponse.getHttpStatus());
	}

	@PostMapping("/add-inventory")
	public ResponseEntity<InventoryDto> addProductInInventory(@RequestBody InventoryDto productRequest) {
		InventoryDto productResponse = inventoryService.addProductInInventory(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@PostMapping("/update-QuantityStock/productId")
	public ResponseEntity<InventoryDto> updateProductQuantityStock(@RequestBody InventoryDto productRequest) {
		InventoryDto productResponse = inventoryService.updateProductQuantityStock(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/getProductsInventories")
	public ResponseEntity<List<InventoryDto>> getProductsInventories() {
		List<InventoryDto> productResponse = inventoryService.getProductsInventories();
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/product-inventory/{productId}")
	public ResponseEntity<InventoryDto> getInventoryByProductId(@PathVariable("productId") long productId) {
		InventoryDto productResponse = inventoryService.getInventoryByProductId(productId);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/inventory-by/{productId}")
	public ResponseEntity<InventoryResponse> inventoryByProductId(@PathVariable("productId") long productId) {
		InventoryResponse productResponse = inventoryService.inventoryByProductId(productId);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
}
