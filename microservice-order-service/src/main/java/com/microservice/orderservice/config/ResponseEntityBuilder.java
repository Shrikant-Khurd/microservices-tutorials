package com.microservice.orderservice.config;

import org.springframework.http.ResponseEntity;

import com.microservice.orderservice.dto.ErrorResponse;

public class ResponseEntityBuilder {

	public static ResponseEntity<Object> build(ErrorResponse apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
