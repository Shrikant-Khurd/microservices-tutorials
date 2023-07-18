package com.mongodb.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class AppConfig {

//  @Bean
//  public CommonsMultipartResolver multipartResolver() {
//    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//    resolver.setDefaultEncoding("utf-8");
//    return resolver;
//  }

  @Bean
  public MultipartResolver multipartResolver() {
      return new StandardServletMultipartResolver();
  }
  
}

