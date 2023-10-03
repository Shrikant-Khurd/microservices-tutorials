package com.microservice.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.inventoryservice.dto.InventoryDto;
import com.microservice.inventoryservice.dto.ProductDto;
import com.microservice.inventoryservice.exception.ResourceNotFoundException;
import com.microservice.inventoryservice.feign.productservice.clients.ProductFeignClient;
import com.microservice.inventoryservice.mapper.InventoryMapper;
import com.microservice.inventoryservice.model.Inventory;
import com.microservice.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
//@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private InventoryRepository inventoryRepository;

	private ProductFeignClient productFeignClient;

	@Autowired
	public InventoryService(InventoryRepository inventoryRepository, ProductFeignClient productFeignClient) {
		super();
		this.inventoryRepository = inventoryRepository;
		this.productFeignClient = productFeignClient;
	}

	public InventoryDto addProductInInventory(InventoryDto inventoryRequest) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryRequest);
		inventory.setInventoryStatus(true);
		Inventory inventorySave = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(inventorySave);
	}

	public List<InventoryDto> getProductsInventories() {
		List<Inventory> inventories = inventoryRepository.findAll();
		List<InventoryDto> inventoryList = new ArrayList<>();

		for (Inventory inventory : inventories) {
			InventoryDto inventoryDto = InventoryMapper.mapToInventoryDto(inventory);
			ProductDto productDto = productFeignClient.getProductDetailByProductId(inventory.getProductId());
			inventoryDto.setProduct(productDto);
			inventoryList.add(inventoryDto);
		}
		return inventoryList;
	}

	public InventoryDto getInventoryByProductId(long productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory.getId() != null) {
			ProductDto productDto = productFeignClient.getProductDetailByProductId(inventory.getProductId());
			InventoryDto inventoryDto = InventoryMapper.mapToInventoryDto(inventory);
			inventoryDto.setProduct(productDto);
			return inventoryDto;
		} else {
			throw new ResourceNotFoundException("Product not found in inventory service...");
		}
	}

	public InventoryDto updateProductQuantityStock(InventoryDto productRequest) {
		Inventory inventory = inventoryRepository.findByProductId(productRequest.getProductId());
		inventory.setQuantityInStock(productRequest.getQuantityInStock());

		if (inventory.getQuantityInStock() <= 0) {
			inventory.setInventoryStatus(false);
			inventory.setQuantityInStock(0);
		} else {
			inventory.setInventoryStatus(true);
		}

		Inventory updateInventory = inventoryRepository.save(inventory);
		return InventoryMapper.mapToInventoryDto(updateInventory);
	}

}
