package com.microservice.usercartservice.feign.userservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.microservice.usercartservice.dto.UserDto;

@FeignClient(name = "auth-service")
public interface UserFeignClient {
	
//	public UserDto getUserDetailByUserId() {
//		// String currentUserId = JwtUtil.GetJwtToken();
//		return restTemplate.postForObject("http://auth-service/api/users/get-user-by-id", null, UserDto.class);
//	}
////	public UserDto getUserDetailByUserId() {
////		Long currentUserId = JwtUtil.getUserIdFromToken();
////		return restTemplate.postForObject("http://user-service/api/users/get-user-by-id/" + currentUserId, null,
////				UserDto.class);
////	}
//
//	public UserDto userDetails() {
//		UserDto userDto = getUserDetailByUserId();
//		CURRENT_USER_ID = userDto.getId();
//		return userDto;
//	}

	
	@PostMapping(value = "api/users/get-user-by-id")
	UserDto getUserDetailByUserId();

}
