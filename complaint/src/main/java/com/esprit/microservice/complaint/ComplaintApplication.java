package com.esprit.microservice.complaint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ComplaintApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintApplication.class, args);
	}

}
