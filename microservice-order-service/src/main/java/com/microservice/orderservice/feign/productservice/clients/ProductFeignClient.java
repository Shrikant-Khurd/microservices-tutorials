package com.microservice.orderservice.feign.productservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.orderservice.dto.ProductDto;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

	@GetMapping(value = "api/product/getProductDetailsById/{productId}")
	ProductDto getProductDetailByProductId(@PathVariable("productId")long productId);
}
