package com.microservice.orderservice.feign.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.microservice.orderservice.dto.AddressDto;
import com.microservice.orderservice.dto.UserDto;

@FeignClient(name = "auth-service")
public interface UserFeignClient {
	
	@PostMapping(value = "api/users/get-user-by-id")
	UserDto getUserDetailByUserId();

	@GetMapping("api/users/address-id/{addressId}")
	AddressDto getAddressDetailByAddressId(@PathVariable("addressId") long addressId);
	
	
}
