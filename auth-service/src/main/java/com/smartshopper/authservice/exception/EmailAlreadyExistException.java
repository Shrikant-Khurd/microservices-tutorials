package com.smartshopper.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyExistException(String msg) {
		super(msg);
	}
}
 