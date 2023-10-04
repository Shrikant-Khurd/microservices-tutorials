package com.smartshopper.inventoryservice.feign.productservice.clients;

import com.smartshopper.inventoryservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

	@GetMapping(value = "api/product/getProductDetailsById/{productId}")
	ProductDto getProductDetailByProductId(@PathVariable("productId")long productId);
}
