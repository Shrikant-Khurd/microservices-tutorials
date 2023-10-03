package com.authservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class InstanceNotAvailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InstanceNotAvailableException(String message) {
		super(message);
	}
}