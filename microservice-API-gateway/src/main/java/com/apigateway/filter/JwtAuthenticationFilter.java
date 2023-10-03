package com.apigateway.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.apigateway.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	@Autowired
	private RestTemplate template;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RouteValidator routeValidator;

	public JwtAuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		return ((exchange, chain) -> {
			if (routeValidator.isSecured.test(exchange.getRequest())) {
				//Header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header...");
				}
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if(authHeader!=null && authHeader.startsWith("Bearer ")) {
					authHeader=authHeader.substring(7);
				}
				try {
					//REST call to AUTH service
					//template.getForObject("http://auth-service/api/auth/authenticate/" + authHeader, String.class);
				jwtUtil.validateToken(authHeader);
				
				} catch (Exception e) {
					System.out.println("Error......");
					throw new RuntimeException("invalid authorization");
				}
			
			}

			return chain.filter(exchange);
		});
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
//		System.out.println(" bearerToken " +bearerToken);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	private String getJwtFromRequest(ServerHttpRequest request) {
		 List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
		 if(!authHeaders.isEmpty()) {
			 String bearerToken = authHeaders.get(0);
			 if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
				 return bearerToken.substring(7, bearerToken.length());
			 }
		 }
		return null;
	}
	
	public static class Config {

	}
	
	
	

}