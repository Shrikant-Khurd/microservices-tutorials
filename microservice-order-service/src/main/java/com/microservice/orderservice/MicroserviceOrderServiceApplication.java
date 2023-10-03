package com.microservice.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


//@EnableJpaRepositories(basePackages = "com.microservice.repository")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MicroserviceOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceOrderServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
