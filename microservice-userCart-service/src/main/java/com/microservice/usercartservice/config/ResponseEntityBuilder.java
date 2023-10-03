package com.microservice.usercartservice.config;

import org.springframework.http.ResponseEntity;

import com.microservice.usercartservice.dto.ErrorResponse;

public class ResponseEntityBuilder {

	public static ResponseEntity<Object> build(ErrorResponse apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
