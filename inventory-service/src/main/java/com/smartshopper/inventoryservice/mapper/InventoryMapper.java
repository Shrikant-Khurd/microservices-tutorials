package com.smartshopper.inventoryservice.mapper;

import com.smartshopper.inventoryservice.dto.InventoryDto;
import com.smartshopper.inventoryservice.model.Inventory;

public class InventoryMapper {

	// Convert Inventory JPA Entity into InventoryDto
	public static InventoryDto mapToInventoryDto(Inventory inventoryRequest) {
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setProductId(inventoryRequest.getProductId());
		inventoryDto.setQuantityInStock(inventoryRequest.getQuantityInStock());
		inventoryDto.setInventoryStatus(inventoryRequest.isInventoryStatus());
		return inventoryDto;
	}

	// Convert InventoryDto into Inventory JPA Entity
	public static Inventory mapToInventory(InventoryDto inventoryRequest) {
		Inventory inventoryDto = new Inventory();
		inventoryDto.setProductId(inventoryRequest.getProductId());
		inventoryDto.setQuantityInStock(inventoryRequest.getQuantityInStock());
		inventoryDto.setInventoryStatus(inventoryRequest.isInventoryStatus());
		return inventoryDto;
	}
}
