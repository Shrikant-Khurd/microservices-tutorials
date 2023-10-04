package com.smartshopper.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
	 public static final List<String> openApiEndpoints = List.of(
	            "/api/auth/authenticate",
	            "/api/auth/register",
	            "/api/auth/roles",
	            "/api/auth/add-role",
	            "/api/auth/current-userRole",
	            "/api/auth/validate",
	            "/api/category/mainCategories",
	            "/api/category/subCategories",
	            "/api/category/secondaryCategories",
	            "/api/product/get-all-products",
	            "/api/product/category",
	            "/api/product/getProductDetailsById",
	            "/api/inventory/get-inventory",
	            "getProductsInventories",
	            "api/inventory/product-inventory/",
	            "api/inventory/inventory-by/"
	    );

	    public Predicate<ServerHttpRequest> isSecured =
	            request -> openApiEndpoints
	                    .stream()
	                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
