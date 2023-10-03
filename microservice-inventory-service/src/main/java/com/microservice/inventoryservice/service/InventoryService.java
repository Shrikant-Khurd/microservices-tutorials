package com.microservice.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.microservice.inventoryservice.dto.InventoryDto;
import com.microservice.inventoryservice.dto.ProductDto;
import com.microservice.inventoryservice.exception.ResourceNotFoundException;
import com.microservice.inventoryservice.mapper.InventoryMapper;
import com.microservice.inventoryservice.model.Inventory;
import com.microservice.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	@Autowired
	private RestTemplate restTemplate;
//	@Transactional(readOnly = true)
//	@SneakyThrows
//	public List<InventoryResponse> isInStock(List<String> skuCode) {
//		log.info("Checking Inventory");
//		return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory -> InventoryResponse.builder()
//				.skuCode(inventory.getSkuCode()).isInStock(inventory.getQuantity() > 0).build()).toList();
//	}

	public ProductDto getProductDetailByProductId(long productId) {
		return restTemplate.getForObject("http://product-service/api/product/getProductDetailsById/" + productId,
				ProductDto.class);
	}

//	@Transactional(readOnly = true)
//	public Boolean isInStock(String skuCode) {
//		return inventoryRepository.findBySkuCode(skuCode).isPresent();
//	}

	public InventoryDto addProductInInventory(InventoryDto inventoryRequest) {
		Inventory inventory = InventoryMapper.mapToInventory(inventoryRequest);
		// ProductDto productDto =
		// getProductDetailByProductId(inventory.getProductId());
		inventory.setInventoryStatus(true);
//		inventory.setProductPrice(productDto.getPrice());
		// inventory.setProductId(inventoryRequest.getProductDto().getProductId());
		Inventory inventorySave = inventoryRepository.save(inventory);
//		ProductDto productDto = getProductDetailByProductId(inventorySave.getProductId());
		return InventoryMapper.mapToInventoryDto(inventorySave);

	}

	public List<InventoryDto> getInventory() {
		List<Inventory> inventories = inventoryRepository.findAll();
		List<InventoryDto> inventoryList = new ArrayList<>();

		for (Inventory inventory : inventories) {
			InventoryDto inventoryDto = InventoryMapper.mapToInventoryDto(inventory);
			// ProductDto productDto =
			// getProductDetailByProductId(inventory.getProductId());
			// inventoryDto.setProduct(productDto);
			inventoryList.add(inventoryDto);
		}
		return inventoryList;
	}

	public List<InventoryDto> getProductsInventories() {
		List<Inventory> inventories = inventoryRepository.findAll();
		List<InventoryDto> inventoryList = new ArrayList<>();

		for (Inventory inventory : inventories) {
			InventoryDto inventoryDto = InventoryMapper.mapToInventoryDto(inventory);
			ProductDto productDto = getProductDetailByProductId(inventory.getProductId());
			inventoryDto.setProduct(productDto);
			inventoryList.add(inventoryDto);
		}
		return inventoryList;
	}

	public InventoryDto getInventoryByProductId(Long productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId);
		if (inventory.getId() != null) {
			ProductDto productDto = getProductDetailByProductId(inventory.getProductId());
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
//		inventory.setInventoryStatus(productRequest.isInventoryStatus());

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
