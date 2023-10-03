package com.microservice.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.inventoryservice.dto.ApiResponse;
import com.microservice.inventoryservice.dto.InventoryDto;
import com.microservice.inventoryservice.dto.InventoryRequestDto;
import com.microservice.inventoryservice.dto.InventoryResponse;
import com.microservice.inventoryservice.dto.InventoryResponseDto;
import com.microservice.inventoryservice.dto.ProductDto;
import com.microservice.inventoryservice.dto.StockDto;
import com.microservice.inventoryservice.exception.ResourceNotFoundException;
import com.microservice.inventoryservice.feign.productservice.clients.ProductFeignClient;
import com.microservice.inventoryservice.mapper.InventoryMapper;
import com.microservice.inventoryservice.model.Inventory;
import com.microservice.inventoryservice.repository.InventoryRepository;
import com.microservice.inventoryservice.utils.ConstantMethods;

@Service
public class InventoryService {

	private InventoryRepository inventoryRepository;

	private ProductFeignClient productFeignClient;

	private MessageSource messageSource;

	@Autowired
	public InventoryService(InventoryRepository inventoryRepository, ProductFeignClient productFeignClient,
			MessageSource messageSource) {
		super();
		this.inventoryRepository = inventoryRepository;
		this.productFeignClient = productFeignClient;
		this.messageSource = messageSource;
	}

	public ApiResponse addProductStock(InventoryRequestDto inventoryRequestDto) {

		Inventory inventoryData = new Inventory();

		for (StockDto stock : inventoryRequestDto.getSizeAndQuantity()) {
			Inventory inventory = new Inventory();
			inventory.setProductId(inventoryRequestDto.getProductId());
			inventory.setSize(stock.getSize());
			inventory.setQuantityInStock(stock.getQuantityInStock());
			inventory.setInventoryStatus(true);
			inventoryData = inventoryRepository.save(inventory);
		}
		InventoryResponseDto responseDto = new InventoryResponseDto();

		List<Inventory> inventories = inventoryRepository.findProductByProductId(inventoryRequestDto.getProductId());

		List<StockDto> stockDtos = new ArrayList<>();

		for (Inventory inventory : inventories) {
			StockDto stock = new StockDto();
			stock.setSize(inventory.getSize());
			stock.setQuantityInStock(inventory.getQuantityInStock());
			stockDtos.add(stock);
		}

		responseDto.setProductId(inventoryData.getProductId());
		responseDto.setSizeAndQuantity(stockDtos);
		responseDto.setInventoryStatus(inventoryData.isInventoryStatus());

		return ConstantMethods.successResponse(responseDto,
				messageSource.getMessage("api.response.stock.added", null, Locale.ENGLISH), HttpStatus.CREATED);
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

	public InventoryResponse  inventoryByProductId(long productId) {
		List<Inventory> inventories = inventoryRepository.findProductByProductId(productId);
		if (!inventories.isEmpty()) {
//			InventoryResponseDto responseDto = new InventoryResponseDto();
//			List<StockDto> stockDtos = new ArrayList<>();
			InventoryResponse responseDto = new InventoryResponse();

			List<InventoryDto> inventoryList = new ArrayList<>();
			
			for (Inventory inventory : inventories) {
//				StockDto stock = new StockDto();
//				stock.setSize(inventory.getSize());
//				stock.setQuantityInStock(inventory.getQuantityInStock());
//				stockDtos.add(stock);
//				responseDto.setInventoryStatus(inventory.isInventoryStatus());
				InventoryDto inventoryDto = InventoryMapper.mapToInventoryDto(inventory);
				inventoryList.add(inventoryDto);
			}
			ProductDto productDto = productFeignClient.getProductDetailByProductId(productId);
			responseDto.setProductId(productId);
			responseDto.setProduct(productDto);
			responseDto.setInventoryDetail(inventoryList);
//			responseDto.setSizeAndQuantity(stockDtos);
			return responseDto;
		} else {
			throw new ResourceNotFoundException("Product not found in inventory service...");
		}
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
