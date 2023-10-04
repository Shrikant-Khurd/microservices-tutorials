package com.smartshopper.productservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
	private int httpStatusCode;
    private HttpStatus status;
    private String message;
    private String error;
    private List<String> errorList;
    
	public ErrorResponse(LocalDateTime timestamp, int httpStatusCode, HttpStatus status, String message,
			String error) {
		super();
		this.timestamp = timestamp;
		this.httpStatusCode = httpStatusCode;
		this.status = status;
		this.error = error;
		this.message = message;
	}
	
	public ErrorResponse(LocalDateTime timestamp, int httpStatusCode, HttpStatus status, String message,
			List<String> errorList) {
		super();
		this.timestamp = timestamp;
		this.httpStatusCode = httpStatusCode;
		this.status = status;
		this.errorList = errorList;
		this.message = message;
	}
    
    
}