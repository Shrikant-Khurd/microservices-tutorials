package com.smartshopper.productservice.feign.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartshopper.productservice.dto.UserDto;

@FeignClient(name = "auth-service")
public interface UserFeignClient {
	
	@PostMapping(value = "api/users/get-user-by-id")
	UserDto getUserDetailByUserId();

}
