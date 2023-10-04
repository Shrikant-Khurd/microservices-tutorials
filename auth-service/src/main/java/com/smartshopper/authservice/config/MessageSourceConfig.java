package com.smartshopper.authservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

//	@Bean
//	public MessageSource messageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasenames("classpath:/messages/api_error_messages",
//				"classpath:/messages/api_response_messages");
//		messageSource.setDefaultEncoding("UTF-8");
//		return messageSource;
//	}
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasename("E:/Java Programs/e-commerce-backend/auth-service/src/main/resources/messages/api_response_messages.properties");
		messageSource.setBasenames("classpath:/messages/api_error_messages",
				"classpath:/messages/api_response_messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//		source.setBasenames("classpath:/messages/api_error_messages",
//				"classpath:/messages/api_response_messages"); // Set the base name of your messages files
//		source.setDefaultEncoding("UTF-8");
//		return source;
//	}

}
