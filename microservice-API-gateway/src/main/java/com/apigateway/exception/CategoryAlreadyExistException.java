package com.apigateway.exception;

public class CategoryAlreadyExistException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistException(String msg) {
		super(msg);
	} 
}
