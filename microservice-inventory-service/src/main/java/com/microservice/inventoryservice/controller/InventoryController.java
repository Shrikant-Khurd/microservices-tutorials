package com.microservice.inventoryservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.inventoryservice.dto.InventoryDto;
import com.microservice.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

	private final InventoryService inventoryService;

	// http://localhost:8082/api/inventory/iphone-13,iphone13-red

	// http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
//        log.info("Received inventory check request for skuCode: {}", skuCode);
//        return inventoryService.isInStock(skuCode);
//    }
//    
//    @GetMapping("/{sku-Code}")
//    @ResponseStatus(HttpStatus.OK)
//    public Boolean isInStock(@PathVariable("sku-code") String skuCode) {
//		return inventoryService.isInStock(skuCode);
//    	
//    }
	@PostMapping("/add-inventory")
	public ResponseEntity<InventoryDto> addProductInInventory(@RequestBody InventoryDto productRequest) {
		InventoryDto productResponse = inventoryService.addProductInInventory(productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@PostMapping("/update-QuantityStock/productId")
	public ResponseEntity<InventoryDto> updateProductQuantityStock(
			@RequestBody InventoryDto productRequest) {
		InventoryDto productResponse = inventoryService.updateProductQuantityStock( productRequest);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/get-inventory")
	public ResponseEntity<List<InventoryDto>> getInventory() {
		List<InventoryDto> productResponse = inventoryService.getInventory();
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
	@GetMapping("/getProductsInventories")
	public ResponseEntity<List<InventoryDto>> getProductsInventories() {
		List<InventoryDto> productResponse = inventoryService.getProductsInventories();
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/product-inventory/{productId}")
	public ResponseEntity<InventoryDto> getInventoryByProductId(@PathVariable("productId") Long productId) {
		InventoryDto productResponse = inventoryService.getInventoryByProductId(productId);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
}
