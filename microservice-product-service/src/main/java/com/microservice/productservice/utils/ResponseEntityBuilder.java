package com.microservice.productservice.utils;

import org.springframework.http.ResponseEntity;

import com.microservice.productservice.dto.ErrorResponse;

public class ResponseEntityBuilder {
	
	public static ResponseEntity<Object> build(ErrorResponse apiError) {
	      return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
