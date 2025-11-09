package com.ticketplatform.user.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.auth-server-url:http://keycloak:8060}")
    private String serverUrl;

    private static final String REALM = "SpeedyGo5se4";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String CLIENT_ID = "admin-cli";

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // L'admin se connecte au realm master
                .clientId(CLIENT_ID)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .grantType("password")
                .build();
    }

    public String createUserWithRole(String email, String password, String firstName, String lastName, String role) {
        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(REALM);
        UsersResource usersResource = realmResource.users();

        // Create user representation
        UserRepresentation user = new UserRepresentation();
        user.setEmail(email);
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Create user in Keycloak
        Response response = usersResource.create(user);
        
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        // Get user ID from location header
        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        // Set password
        UserResource userResource = usersResource.get(userId);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        userResource.resetPassword(credential);

        // Assign role (client, driver, or admin allowed)
        if (role.equals("client") || role.equals("driver") || role.equals("admin")) {
            RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
            System.out.println("✅ Assigned role '" + role + "' to user: " + email);
        } else {
            // Default to client role if invalid role provided
            RoleRepresentation roleRepresentation = realmResource.roles().get("client").toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
            System.out.println("⚠️ Invalid role, assigned default 'client' role to user: " + email);
        }

        keycloak.close();
        System.out.println("✅ Created user in Keycloak: " + firstName + " " + lastName + " (" + email + ")");
        
        return userId;
    }
}

