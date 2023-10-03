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
		productFallbackResponse.setMessage("Product Service is down! Please try later");

		return new ResponseEntity<>(productFallbackResponse, productFallbackResponse.getHttpStatus());

	}
	@GetMapping("/inventory-fallback")
	public ResponseEntity<ApiResponse> inventoryFallback() {
		
		ApiResponse inventoryFallbackResponse = new ApiResponse();
		inventoryFallbackResponse.setTimeStamp(new Date());
		inventoryFallbackResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		inventoryFallbackResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		inventoryFallbackResponse.setMessage("Inventory Service is down! Please try later");
		
		return new ResponseEntity<>(inventoryFallbackResponse, inventoryFallbackResponse.getHttpStatus());
		
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

	@GetMapping("/cart-service-fallback")
	public ResponseEntity<ApiResponse> cartFallback() {

		ApiResponse cartFallbackResponse = new ApiResponse();
		cartFallbackResponse.setTimeStamp(new Date());
		cartFallbackResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		cartFallbackResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		cartFallbackResponse.setMessage("Cart Service is down! Please try later");

		return new ResponseEntity<>(cartFallbackResponse, cartFallbackResponse.getHttpStatus());

	}
}