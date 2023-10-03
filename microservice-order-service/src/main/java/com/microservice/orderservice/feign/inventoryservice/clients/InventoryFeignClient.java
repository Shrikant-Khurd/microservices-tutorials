package com.microservice.orderservice.feign.inventoryservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservice.orderservice.dto.InventoryDto;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

//	public InventoryDto ProductInventoryDetails(long productId) {
//	return restTemplate.getForObject("http://inventory-service/api/inventory/product-inventory/" + productId,
//			InventoryDto.class);
//}
	
	@GetMapping(value = "api/inventory/product-inventory/{productId}")
	InventoryDto productInventoryDetails(@PathVariable("productId")long productId);
	
	
	@PostMapping("/update-QuantityStock/productId")
	public InventoryDto updateProductQuantityStock(@RequestBody InventoryDto inventoryRequest);
}


