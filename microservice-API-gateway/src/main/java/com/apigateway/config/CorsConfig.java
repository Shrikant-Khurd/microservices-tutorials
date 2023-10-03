package com.apigateway.config;

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
	            .allowedOrigins("*") // Allow all origins for open API endpoints
	            .allowedMethods("*")
	            .allowedHeaders("*")
	            .exposedHeaders("Authorization")
	            .allowCredentials(true)
	            .maxAge(3600)
	            .allowedMethods("GET", "POST", "PUT", "DELETE");
	    }
	
	/*@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST"));
		config.setAllowedHeaders(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}*/

}
