package com.microservice.inventoryservice.exception;

public class RecordNotFoundException extends RuntimeException{

	public RecordNotFoundException(String msg) {
		super(msg);
	}
}
 