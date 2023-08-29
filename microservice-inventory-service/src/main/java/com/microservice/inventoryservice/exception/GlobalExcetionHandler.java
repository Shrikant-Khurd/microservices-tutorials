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
		return new ResponseEntity<ApiResponse>(resourceNotFoundResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(InstanceNotAvailableException.class)
	public ResponseEntity<ApiResponse> handleInstanceNotAvailableException(InstanceNotAvailableException exception,
			WebRequest request) {
		ApiResponse InstanceNotAvailableResponse = new ApiResponse();
		// resourceNotFoundResponse.setData(null);
		InstanceNotAvailableResponse.setTimeStamp(new Date());
		InstanceNotAvailableResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		InstanceNotAvailableResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		InstanceNotAvailableResponse.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<ApiResponse>(InstanceNotAvailableResponse, HttpStatus.SERVICE_UNAVAILABLE);

	}

	


}
