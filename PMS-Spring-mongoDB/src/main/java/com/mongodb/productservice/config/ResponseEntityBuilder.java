package com.mongodb.productservice.config;

import org.springframework.http.ResponseEntity;

import com.mongodb.productservice.dto.ErrorResponse;

public class ResponseEntityBuilder {
	
	public static ResponseEntity<Object> build(ErrorResponse apiError) {
	      return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
