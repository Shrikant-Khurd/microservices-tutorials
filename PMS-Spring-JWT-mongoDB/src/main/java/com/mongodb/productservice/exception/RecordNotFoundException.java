package com.mongodb.productservice.exception;

public class RecordNotFoundException extends RuntimeException{

	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
 