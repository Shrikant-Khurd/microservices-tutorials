package com.smartshopper.productservice.feign.inventoryservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smartshopper.productservice.dto.InventoryDto;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

	@PostMapping("/api/inventory/add-inventory")
	public InventoryDto addProductInInventory(@RequestBody InventoryDto inventoryRequest);

}
