package com.microservice.productservice.feign.inventoryservice.clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.microservice.productservice.dto.InventoryDto;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {
	
//	@Autowired
//	public  RestTemplate restTemplate;
//	public default InventoryDto ProductInventoryDetails(long productId) {
//	return restTemplate.getForObject("http://inventory-service/api/inventory/product-inventory/" + productId,
//			InventoryDto.class);
//}
//	public static List<InventoryDto> inventoryDetails() {
//		InventoryDto[] inventoryData = restTemplate.getForObject("http://inventory-service/api/inventory/get-inventory",
//				InventoryDto[].class);
//		List<InventoryDto> inventoryList = new ArrayList<InventoryDto>();
//		for (InventoryDto inventoryDto : inventoryData) {
//			inventoryList.add(inventoryDto);
//		}
//		return inventoryList;
//	}

	@PostMapping("/add-inventory")
	public InventoryDto addProductInInventory(@RequestBody InventoryDto inventoryRequest);



}
