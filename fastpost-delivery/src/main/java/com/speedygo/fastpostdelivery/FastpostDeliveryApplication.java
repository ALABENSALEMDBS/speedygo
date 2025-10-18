package com.speedygo.fastpostdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FastpostDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastpostDeliveryApplication.class, args);
	}

}
