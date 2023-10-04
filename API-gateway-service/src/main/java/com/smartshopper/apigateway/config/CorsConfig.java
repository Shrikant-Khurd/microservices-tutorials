package com.smartshopper.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer{
	
	 @Override
	 public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	            .allowedOrigins("http://localhost:4200")
	            .allowedMethods("*")
	            .allowedHeaders("*")
	            .exposedHeaders("Authorization")
	            .allowCredentials(true)
	            .maxAge(3600);

	        registry.addMapping("/api/**")
	            .allowedOrigins("*")
	            .allowedMethods("*")
	            .allowedHeaders("*")
	            .exposedHeaders("Authorization")
	            .allowCredentials(true)
	            .maxAge(3600)
	            .allowedMethods("GET", "POST", "PUT", "DELETE");
	    }
}
