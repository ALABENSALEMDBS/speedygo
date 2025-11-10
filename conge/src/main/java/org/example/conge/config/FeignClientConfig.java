package org.example.conge.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // Get the current authentication from SecurityContext
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication != null && authentication instanceof JwtAuthenticationToken) {
                    JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                    Jwt jwt = jwtAuth.getToken();
                    
                    // Add the Authorization header with the Bearer token
                    requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
                    
                    System.out.println("✅ Feign: Added Authorization header to request");
                } else {
                    System.out.println("⚠️ Feign: No JWT token found in SecurityContext");
                }
            }
        };
    }
}

