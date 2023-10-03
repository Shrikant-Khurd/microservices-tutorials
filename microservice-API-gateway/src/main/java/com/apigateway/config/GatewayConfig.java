package com.apigateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.apigateway.filter.AuthenticationFilter;
import com.apigateway.util.JwtUtil;

@Configuration
public class GatewayConfig {

	@Autowired
	private AuthenticationFilter filter;
	@Autowired
	private JwtUtil jwtUtil;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				// AUTH-USER-SERVICE - port 1010
				.route("auth-service", r -> r.method(HttpMethod.POST, HttpMethod.GET).and().path("/api/auth/**")
						.filters(f -> f.filter(filter)
								.circuitBreaker(config -> config.setName("user-service-circuit-breaker")
										.setFallbackUri("forward:/user-auth-fallback")))
						.uri("lb://auth-service"))

				// AUTH-USER-SERVICE - port 1010

				.route("auth-service", r -> r.method(HttpMethod.POST, HttpMethod.GET).and().path("/api/users/**")
						.filters(f -> f.filter(filter).filter(applyRoleBasedAuthorization("ADMIN", "USER"))
								.circuitBreaker(config -> config.setName("user-service-circuit-breaker")
										.setFallbackUri("forward:/user-auth-fallback")))
						.uri("lb://auth-service"))

				// PRODUCT-SERVICE - port 7171

				.route("product-service",
						r -> r.method(HttpMethod.GET).and().path("/api/product/**", "/api/category/**")
								.filters(f -> f.filter(filter)
										.circuitBreaker(config -> config.setName("product-service-circuit-breaker")
												.setFallbackUri("forward:/product-fallback")))
								.uri("lb://product-service"))

				.route("product-service",
						r -> r.method(HttpMethod.POST).and().path("/api/product/**", "/api/category/**")
								.filters(f -> f.filter(filter).filter(applyRoleBasedAuthorization("ADMIN"))
										.circuitBreaker(config -> config.setName("product-service-circuit-breaker")
												.setFallbackUri("forward:/product-fallback")))
								.uri("lb://product-service"))

				// INVENTORY-SERVICE - port 7174

				.route("inventory-service", r -> r.method(HttpMethod.GET).and().path("/api/inventory/**")
						.filters(f -> f.filter(filter)
								.circuitBreaker(config -> config.setName("inventory-service-circuit-breaker")
										.setFallbackUri("forward:/inventory-fallback")))
						.uri("lb://inventory-service"))

				.route("inventory-service", r -> r.method(HttpMethod.POST).and().path("/api/inventory/**")
						.filters(f -> f.filter(filter).filter(applyRoleBasedAuthorization("ADMIN", "USER"))
								.circuitBreaker(config -> config.setName("inventory-service-circuit-breaker")
										.setFallbackUri("forward:/inventory-fallback")))
						.uri("lb://inventory-service"))

				// CART-SERVICE - port 7173

				.route("cart-service", r -> r.method(HttpMethod.POST, HttpMethod.GET).and().path("/api/carts/**")
						.filters(f -> f.filter(filter).filter(applyRoleBasedAuthorization("ADMIN", "USER"))
								.circuitBreaker(config -> config.setName("cart-service-circuit-breaker")
										.setFallbackUri("forward:/cart-service-fallback")))
						.uri("lb://cart-service"))

				// ORDER-SERVICE - port 7172

				.route("order-service", r -> r.method(HttpMethod.POST, HttpMethod.GET).and().path("/api/order/**")
						.filters(f -> f.filter(filter).filter(applyRoleBasedAuthorization("ADMIN", "USER"))
								.circuitBreaker(config -> config.setName("order-service-circuit-breaker")
										.setFallbackUri("forward:/order-service-fallback")))
						.uri("lb://order-service"))

				.build();
	}

	private GatewayFilter applyRoleBasedAuthorization(String... roles) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			final String token = filter.getJwtFromRequest(request);
			if (token != null && jwtUtil.validateJwtToken(token)) {
				List<String> userRoles = exchange.getRequest().getHeaders().get("Role");
				for (String role : roles) {
					if (userRoles != null && userRoles.contains(role)) {
						return chain.filter(exchange);
					}
				}
			}
			return filter.handleInvalidTokenResponse(exchange, "You dont have access..");
		};
	}
}