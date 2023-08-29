package com.apigateway.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.apigateway.util.ResponseEntityBuilder;
import com.microservice.productservice.dto.ApiResponse;
import com.microservice.productservice.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExcetionHandler extends ResponseEntityExceptionHandler {

	// handleHttpMediaTypeNotSupported : triggers when the JSON is invalid
//	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		details.add(builder.toString());

		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST, "Invalid JSON", details);

		return ResponseEntityBuilder.build(err);

	}

	// handleHttpMessageNotReadable : triggers when the JSON is malformed
	// @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST, "Malformed JSON request", details);

		return ResponseEntityBuilder.build(err);
	}

	// handleMethodArgumentNotValid : triggers when @Valid fails
//	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getObjectName() + " : " + error.getDefaultMessage()).collect(Collectors.toList());

		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST, "Validation Errors", details);

		return ResponseEntityBuilder.build(err);
	}

	// handleMissingServletRequestParameter : triggers when there are missing
	// parameters
//	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getParameterName() + " parameter is missing");

		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST, "Missing Parameters", details);

		return ResponseEntityBuilder.build(err);
	}

	// handleMethodArgumentTypeMismatch : triggers when a parameter's type does not
	// match
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		// List<String> details = new ArrayList<String>();
		// details.add(ex.getMessage());

		ErrorResponse err = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST, ex.getMessage(), "Mismatch Type");

		return ResponseEntityBuilder.build(err);
	}

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

	@ExceptionHandler(CategoryAlreadyExistException.class)
	public ResponseEntity<ApiResponse> handleCategoryAlreadyExistException(CategoryAlreadyExistException ex,
			WebRequest request) {
		String requestPath = request.getDescription(false);
		ApiResponse categoryAlreadyExistResponse = new ApiResponse();
		// resourceNotFoundResponse.setData(null);
		categoryAlreadyExistResponse.setTimeStamp(new Date());
		categoryAlreadyExistResponse.setHttpStatus(HttpStatus.CONFLICT);
		categoryAlreadyExistResponse.setHttpStatusCode(HttpStatus.CONFLICT.value());
		categoryAlreadyExistResponse.setMessage(ex.getLocalizedMessage());
		return new ResponseEntity<ApiResponse>(categoryAlreadyExistResponse, HttpStatus.CONFLICT);

	}

	
}
