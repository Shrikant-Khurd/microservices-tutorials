package com.apigateway.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.productservice.dto.ApiResponse;

@RestController
public class FallbackController {

	@PostMapping("/user-auth-fallback")
	public ResponseEntity<ApiResponse> authFallback() {

		ApiResponse authFallbackResponse = new ApiResponse();
		authFallbackResponse.setTimeStamp(new Date());
		authFallbackResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		authFallbackResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		authFallbackResponse.setMessage("User Auth Service is down! Please try later");

		return new ResponseEntity<>(authFallbackResponse, authFallbackResponse.getHttpStatus());
	}

	@GetMapping("/product-fallback")
	public ResponseEntity<ApiResponse> productFallback() {

		ApiResponse productFallbackResponse = new ApiResponse();
		productFallbackResponse.setTimeStamp(new Date());
		productFallbackResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		productFallbackResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		productFallbackResponse.setMessage("product Service is down! Please try later");

		return new ResponseEntity<>(productFallbackResponse, productFallbackResponse.getHttpStatus());

	}
	@GetMapping("/order-service-fallback")
	public ResponseEntity<ApiResponse> orderFallback() {
		
		ApiResponse orderFallbackResponse = new ApiResponse();
		orderFallbackResponse.setTimeStamp(new Date());
		orderFallbackResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		orderFallbackResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		orderFallbackResponse.setMessage("Order Service is down! Please try later");
		
		return new ResponseEntity<>(orderFallbackResponse, orderFallbackResponse.getHttpStatus());
		
	}
}