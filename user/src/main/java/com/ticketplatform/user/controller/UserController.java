package com.ticketplatform.user.controller;

import com.ticketplatform.user.dto.RegisterRequest;
import com.ticketplatform.user.entity.User;
import com.ticketplatform.user.service.UserService;
import com.ticketplatform.user.service.KeycloakUserService;
import com.ticketplatform.user.service.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    // Register new user with role selection
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Create user in Keycloak with selected role
            String keycloakUserId = keycloakAdminService.createUserWithRole(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getRole()
            );

            // Create user in local database
            User user = new User();
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setActive(true);
            user.setRoles(List.of(request.getRole()));
            userService.createUser(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully with role: " + request.getRole());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    // Get or create current user from JWT
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("preferred_username");
        List<String> roles = Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .map(m -> (List<String>) m.get("roles"))
                .orElse(List.of());

        User user = keycloakUserService.getOrCreateUser(userId, firstName, lastName, email, username, roles);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/assignVehicle/{vehicleId}")
    public ResponseEntity<?> assignVehicleToUser(@PathVariable Long id, @PathVariable String vehicleId) {
        return userService.updateAssignedVehicle(id, vehicleId)
                ? ResponseEntity.ok("Vehicle assigned")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PutMapping("/{id}/dailyDeliveries")
    public ResponseEntity<?> updateDailyDeliveriesCount(@PathVariable Long id, @RequestParam("count") int count) {
        return userService.updateDailyDeliveriesCount(id, count)
                ? ResponseEntity.ok("Daily deliveries count updated")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

}

