package com.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistException(String msg) {
		super(msg);
	} 
}
