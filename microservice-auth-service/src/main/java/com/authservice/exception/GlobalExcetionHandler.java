package com.authservice.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.authservice.dto.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExcetionHandler extends ResponseEntityExceptionHandler {

	// handleResourceNotFoundException : triggers when there is not resource with
	// the specified ID in BDD
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		ApiResponse resourceNotFoundResponse = new ApiResponse();
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
		instanceNotAvailableResponse.setTimeStamp(new Date());
		instanceNotAvailableResponse.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
		instanceNotAvailableResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		instanceNotAvailableResponse.setMessage(exception.getLocalizedMessage());
		return new ResponseEntity<>(instanceNotAvailableResponse, HttpStatus.SERVICE_UNAVAILABLE);

	}

	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<ApiResponse> handleEmailAlreadyExistException(EmailAlreadyExistException ex,
			WebRequest request) {
		String requestPath = request.getDescription(false);

		ApiResponse emailAlreadyExistResponse = new ApiResponse();
		emailAlreadyExistResponse.setTimeStamp(new Date());
		emailAlreadyExistResponse.setHttpStatus(HttpStatus.CONFLICT);
		emailAlreadyExistResponse.setHttpStatusCode(HttpStatus.CONFLICT.value());
		emailAlreadyExistResponse.setMessage(ex.getLocalizedMessage());
		emailAlreadyExistResponse.setPath(requestPath);
		return new ResponseEntity<>(emailAlreadyExistResponse, HttpStatus.CONFLICT);

	}

	@ExceptionHandler(CategoryAlreadyExistException.class)
	public ResponseEntity<ApiResponse> handleCategoryAlreadyExistException(CategoryAlreadyExistException ex,
			WebRequest request) {
		String requestPath = request.getDescription(false);
		ApiResponse categoryAlreadyExistResponse = new ApiResponse();
		categoryAlreadyExistResponse.setTimeStamp(new Date());
		categoryAlreadyExistResponse.setHttpStatus(HttpStatus.CONFLICT);
		categoryAlreadyExistResponse.setHttpStatusCode(HttpStatus.CONFLICT.value());
		categoryAlreadyExistResponse.setMessage(ex.getLocalizedMessage());
		categoryAlreadyExistResponse.setPath(requestPath);
		return new ResponseEntity<>(categoryAlreadyExistResponse, HttpStatus.CONFLICT);

	}

	// handleNoHandlerFoundException : triggers when the handler method is invalid
	// @Override
	protected ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException ex,
			WebRequest request) {
		String errorMessage = String.format("Could not find the %s method for URL %s", ex.getHttpMethod(),
				ex.getRequestURL());
		String requestPath = request.getDescription(false);
		ApiResponse errorResponse = new ApiResponse();
		errorResponse.setTimeStamp(new Date());
		errorResponse.setHttpStatus(HttpStatus.CONFLICT);
		errorResponse.setHttpStatusCode(HttpStatus.CONFLICT.value());
		errorResponse.setMessage(errorMessage);
		errorResponse.setPath(requestPath);
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = { ExpiredJwtException.class })
	public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
		String requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
		ApiResponse resourceNotFoundResponse = new ApiResponse();
		resourceNotFoundResponse.setTimeStamp(new Date());
		resourceNotFoundResponse.setHttpStatus(HttpStatus.FORBIDDEN);
		resourceNotFoundResponse.setHttpStatusCode(HttpStatus.FORBIDDEN.value());
		resourceNotFoundResponse.setPath(requestUri);
		resourceNotFoundResponse.setMessage(ex.getLocalizedMessage());

		return new ResponseEntity<>(resourceNotFoundResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
	}
}
