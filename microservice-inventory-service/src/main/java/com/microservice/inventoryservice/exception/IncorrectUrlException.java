package com.microservice.inventoryservice.exception;


import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class IncorrectUrlException extends MethodArgumentTypeMismatchException {

	private static final long serialVersionUID = 1L;
	private static Class<?> requiredType;
	private static MethodParameter parameter;
	private static Throwable cause;

	public IncorrectUrlException(String msg) {
		super(msg, requiredType, msg, parameter, cause);
	}

}
