package com.smartshopper.orderservice.feign.usercartservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartshopper.orderservice.dto.AddressDto;
import com.smartshopper.orderservice.dto.ApiResponse;
import com.smartshopper.orderservice.dto.UserDto;

@FeignClient(name = "cart-service")
public interface UserCartFeignClient {

	@PostMapping(value = "api/carts/removeProductFromOrderAndCart/{productId}")
	ApiResponse removeCartByProductId(@PathVariable(name = "productId") Long productId);

}
