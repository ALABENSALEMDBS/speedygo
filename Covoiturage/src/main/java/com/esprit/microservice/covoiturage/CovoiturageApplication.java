package com.esprit.microservice.covoiturage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CovoiturageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovoiturageApplication.class, args);
	}

}
