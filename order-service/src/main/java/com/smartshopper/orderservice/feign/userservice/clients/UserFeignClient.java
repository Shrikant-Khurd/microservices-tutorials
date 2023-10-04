package com.smartshopper.orderservice.feign.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartshopper.orderservice.dto.AddressDto;
import com.smartshopper.orderservice.dto.UserDto;

@FeignClient(name = "auth-service")
public interface UserFeignClient {

	@PostMapping("api/users/get-user-by-id")
	UserDto getUserDetailByUserId();

	@GetMapping("api/users/address-id/{addressId}")
	AddressDto getAddressDetailByAddressId(@PathVariable("addressId") long addressId);

}
