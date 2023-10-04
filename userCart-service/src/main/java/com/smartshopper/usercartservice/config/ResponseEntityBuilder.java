package com.smartshopper.usercartservice.config;

import org.springframework.http.ResponseEntity;

import com.smartshopper.usercartservice.dto.ErrorResponse;

public class ResponseEntityBuilder {

	public static ResponseEntity<Object> build(ErrorResponse apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
