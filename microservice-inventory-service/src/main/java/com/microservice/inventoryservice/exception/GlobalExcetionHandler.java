package com.microservice.inventoryservice.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.microservice.inventoryservice.dto.ApiResponse;

@ControllerAdvice
public class GlobalExcetionHandler extends ResponseEntityExceptionHandler {

	// handleResourceNotFoundException : triggers when there is not resource with
	// the specified ID in BDD
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ApiResponse resourceNotFoundResponse = new ApiResponse();
		// resourceNotFoundResponse.setData(null);
		resourceNotFoundResponse.setTimeStamp(new Date());
		resourceNotFoundResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
		resourceNotFoundResponse.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
		resourceNotFoundResponse.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<>(resourceNotFoundResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(InstanceNotAvailableException.class)
	public ResponseEntity<ApiResponse> handleInstanceNotAvailableException(InstanceNotAvailableException exception,
			WebRequest request) {
		ApiResponse instanceNotAvailableResponse = new ApiResponse();
		// resourceNotFoundResponse.setData(null);
		instanceNotAvailableResponse.setTimeStamp(new Date());
		instanceNotAvailableResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		instanceNotAvailableResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		instanceNotAvailableResponse.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<>(instanceNotAvailableResponse, HttpStatus.SERVICE_UNAVAILABLE);

	}

}
