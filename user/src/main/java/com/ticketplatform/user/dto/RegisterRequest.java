package com.ticketplatform.user.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role; // "client" or "driver"
}

