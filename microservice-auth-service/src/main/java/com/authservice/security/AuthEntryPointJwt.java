package com.authservice.security;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.authservice.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        ApiResponse expiredTokenResponse = new ApiResponse();
		expiredTokenResponse.setTimeStamp(new Date());
		expiredTokenResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
		expiredTokenResponse.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
		expiredTokenResponse.setMessage("You dont have any right.");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ObjectMapper objectMapper = new ObjectMapper();
		String errorResponse = objectMapper.writeValueAsString(expiredTokenResponse);

		response.getWriter().write(errorResponse);
    }
}
