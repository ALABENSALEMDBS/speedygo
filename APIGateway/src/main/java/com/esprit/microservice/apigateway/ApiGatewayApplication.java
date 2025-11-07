package com.esprit.microservice.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("Covoiturage",r->r.path("/api/carpooling/**")
                        .uri("lb://Covoiturage") )

                .route("Vehicules", r->r.path("/Vehicules/api/vehicle/**")
                        .uri("lb://Vehicules") )

                .route("complaint", r->r.path("/complaint/api/complaints/**")
                        .uri("lb://complaint") )

                .route("fastpost-delivery", r->r.path("/api/fastpost/**")
                        .uri("lb://fastpost-delivery") )

                .route("delivery", r->r.path("/api/delivery/**")
                        .uri("lb://fastpost-delivery") )

                .route("delivery-fastpost", r->r.path("/api/delivery-fastpost/**")
                        .uri("lb://fastpost-delivery") )

                .route("ServiceProduit", r->r.path("/api/Produit/**")
                        .uri("lb://ServiceProduit") )

                .route("conge", r->r.path("/conge/leaveSettings/**")
                        .uri("lb://conge") )

                .route("paiment", r->r.path("/paiment/ads/**")
                        .uri("lb://paiment") )
                .route("user", r->r.path("/api/users/**")
                        .uri("lb://user") )
                .route("user", r->r.path("/api/auth/**")
                        .uri("lb://user") )


                .build();
    }
}
