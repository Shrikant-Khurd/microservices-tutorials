package com.userservice.config;

import org.springframework.http.ResponseEntity;

import com.userservice.dto.ErrorResponse;

public class ResponseEntityBuilder {
	
	public static ResponseEntity<Object> build(ErrorResponse apiError) {
	      return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
