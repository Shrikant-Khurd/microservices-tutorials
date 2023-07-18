package com.apigateway.exception;

public class RecordNotFoundException extends RuntimeException{

	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
 