package com.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.apigateway.exception.CategoryAlreadyExistException;
import com.apigateway.exception.RecordNotFoundException;
import com.apigateway.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.productservice.dto.ApiResponse;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter {

	@Autowired
	private JwtUtil tokenHelper;

	@Autowired
	private RouteValidator routerValidator;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		if (routerValidator.isSecured.test(request)) {

			final String token = getJwtFromRequest(request);

			if (token == null) {
				System.out.println("Authorization header/token is missing in request");
				return this.handleInvalidTokenResponse(exchange, "Authorization header/token is missing in request");
			//	throw new CategoryAlreadyExistException("Authorization header/token is missing in request");
			}
			/*
			 * if (Boolean.FALSE.equals(tokenHelper.isTokenExpired(token))) return
			 * this.handleInvalidTokenResponse(exchange,
			 * "Session is expired. Please try logging in again.");
			 * 
			 * if (Boolean.FALSE.equals(tokenHelper.validateJwtToken(token))) return
			 * this.handleInvalidTokenResponse(exchange, "Authorization token is invalid");
			 */
			if (!tokenHelper.validateJwtToken(token))
				return this.handleInvalidTokenResponse(exchange, "Authorization token is invalid");

			if (!tokenHelper.isTokenExpired(token))
				return this.handleInvalidTokenResponse(exchange, "Session is expired. Please try logging in again.");

			this.populateRequestWithHeaders(exchange, token);
		}
		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);

		return response.setComplete();
	}

	public Mono<Void> handleInvalidTokenResponse(ServerWebExchange exchange, String message) {
		
	//	 throw new RecordNotFoundException(message);
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		ApiResponse expiredTokenResponse = new ApiResponse();
		expiredTokenResponse.setTimeStamp(new Date());
		expiredTokenResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
		expiredTokenResponse.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
		expiredTokenResponse.setMessage(message);

		ObjectMapper objectMapper = new ObjectMapper();
		String errorResponse;
		try {
			errorResponse = objectMapper.writeValueAsString(expiredTokenResponse);
		} catch (JsonProcessingException e) {
			errorResponse = "";
			e.printStackTrace();
		}

		DataBuffer buffer = response.bufferFactory().wrap(errorResponse.getBytes(StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(buffer));
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
		Claims claims = tokenHelper.getAllClaimsFromToken(token);
		exchange.getRequest().mutate().header("id", String.valueOf(claims.getSubject()))
				.header("role", String.valueOf(claims.get("role"))).build();
	}

	public String getJwtFromRequest(ServerHttpRequest request) {
		List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
		if (!authHeaders.isEmpty()) {
			String bearerToken = authHeaders.get(0);
			if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
				return bearerToken.substring(7, bearerToken.length());
			}
		}
		return null;
	}
}