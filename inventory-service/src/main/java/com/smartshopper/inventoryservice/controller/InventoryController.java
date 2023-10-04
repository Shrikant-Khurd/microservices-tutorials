package com.smartshopper.inventoryservice.controller;

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

import com.smartshopper.inventoryservice.dto.InventoryDto;
import com.smartshopper.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j 
public class InventoryController {

	private final InventoryService inventoryService;

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
}
