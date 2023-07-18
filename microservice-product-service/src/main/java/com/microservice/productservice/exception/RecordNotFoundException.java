package com.microservice.productservice.exception;

public class RecordNotFoundException extends RuntimeException{

	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
 