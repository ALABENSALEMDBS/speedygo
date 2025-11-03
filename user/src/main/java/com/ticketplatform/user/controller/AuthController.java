package com.ticketplatform.user.controller;

import com.ticketplatform.user.dto.LoginRequest;
import com.ticketplatform.user.dto.LoginResponse;
import com.ticketplatform.user.service.KeycloakAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return keycloakAuthService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}

