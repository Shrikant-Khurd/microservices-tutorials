package com.smartshopper.productservice.utils;

import org.springframework.http.ResponseEntity;

import com.smartshopper.productservice.dto.ErrorResponse;

public class ResponseEntityBuilder {
	
	public static ResponseEntity<Object> build(ErrorResponse apiError) {
	      return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
