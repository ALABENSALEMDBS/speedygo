package com.ticketplatform.user.service;

import com.ticketplatform.user.dto.LoginRequest;
import com.ticketplatform.user.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret:}")
    private String clientSecret;

    private static final String GRANT_TYPE = "password";

    public LoginResponse login(String email, String password) {
        String tokenUrl = serverUrl.endsWith("/")
                ? serverUrl + "realms/" + realm + "/protocol/openid-connect/token"
                : serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        System.out.println("🔐 Login attempt:");
        System.out.println("   URL: " + tokenUrl);
        System.out.println("   Email: " + email);
        System.out.println("   Client ID: " + clientId);
        System.out.println("   Client Secret: " + (clientSecret != null && !clientSecret.isBlank() ? "***SET***" : "NOT SET"));

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GRANT_TYPE);
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("username", email);
        form.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();

        try {
            System.out.println("📤 Sending login request to Keycloak...");
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                    tokenUrl,
                    new HttpEntity<>(form, headers),
                    LoginResponse.class
            );

            System.out.println("📥 Response status: " + response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.err.println("❌ Login failed: Bad response from Keycloak");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
            }

            System.out.println("✅ Login successful for user: " + email);
            return response.getBody();

        } catch (Exception e) {
            System.err.println("❌ Login failed for user: " + email);
            System.err.println("   Error type: " + e.getClass().getName());
            System.err.println("   Error message: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials: " + e.getMessage());
        }
    }
}

