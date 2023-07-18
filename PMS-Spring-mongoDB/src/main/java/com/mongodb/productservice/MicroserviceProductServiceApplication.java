package com.mongodb.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class MicroserviceProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductServiceApplication.class, args);
	}

	
}
