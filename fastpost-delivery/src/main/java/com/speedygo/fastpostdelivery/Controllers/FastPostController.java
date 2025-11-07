package com.speedygo.fastpostdelivery.Controllers;

import com.speedygo.fastpostdelivery.Services.FastPostService;
import com.speedygo.fastpostdelivery.entities.FastPost;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fastpost")

public class FastPostController {

    @Autowired
    private FastPostService fastPostService;

    @GetMapping
    public List<FastPost> getAllFastPosts() {
        return fastPostService.getAllFastPosts();
    }

    @GetMapping("/{id}")
    public FastPost getFastPost(@PathVariable Long id) {
        return fastPostService.getFastPost(id);
    }

    @PostMapping
    public ResponseEntity<?> addFastPost(@RequestBody FastPost fastPost, @AuthenticationPrincipal Jwt jwt) {
        try {
            // Set the user ID from the authenticated user
            if (jwt != null) {
                try {
                    String userId = jwt.getClaim("sub");
                    fastPost.setUserId(userId);

                    // Extract user information from JWT - handle null values
                    String firstName = jwt.getClaim("given_name");
                    String lastName = jwt.getClaim("family_name");
                    String preferredUsername = jwt.getClaim("preferred_username");
                    String email = jwt.getClaim("email");

                    // Set firstName - use preferred_username or email if given_name is null
                    if (firstName != null && !firstName.isEmpty()) {
                        fastPost.setClientFirstName(firstName);
                    } else if (preferredUsername != null && !preferredUsername.isEmpty()) {
                        fastPost.setClientFirstName(preferredUsername);
                    } else if (email != null && !email.isEmpty()) {
                        fastPost.setClientFirstName(email.split("@")[0]);
                    } else {
                        fastPost.setClientFirstName("Client");
                    }

                    // Set lastName - use empty string if null
                    if (lastName != null && !lastName.isEmpty()) {
                        fastPost.setClientLastName(lastName);
                    } else {
                        fastPost.setClientLastName("");
                    }

                    System.out.println("✅ Setting user ID for FastPost: " + userId);
                    System.out.println("✅ User information: " + fastPost.getClientFirstName() + " " + fastPost.getClientLastName());
                } catch (Exception e) {
                    System.out.println("⚠️ Error extracting JWT claims: " + e.getMessage());
                    e.printStackTrace();
                    fastPost.setClientFirstName("Client");
                    fastPost.setClientLastName("Unknown");
                }
            } else {
                System.out.println("❌ No JWT token found, user ID not set for FastPost");
                fastPost.setClientFirstName("Client");
                fastPost.setClientLastName("Unknown");
            }

            FastPost savedFastPost = fastPostService.addFastPost(fastPost);
            return ResponseEntity.ok(savedFastPost);
        } catch (Exception e) {
            System.out.println("❌ Error creating FastPost: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating FastPost: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public FastPost modifyFastPost(@PathVariable Long id, @RequestBody FastPost fastPost) {
        return fastPostService.modifyFastPost(id, fastPost);
    }

    @DeleteMapping("/{id}")
    public void deleteFastPost(@PathVariable Long id) {
        fastPostService.deleteFastPost(id);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approveFastPost(@PathVariable Long id) {
        fastPostService.approveFastPost(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> rejectFastPost(@PathVariable Long id) {
        fastPostService.rejectFastPost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/approved")
    public List<FastPost> getApprovedFastPosts() {
        return fastPostService.getApprovedFastPosts();
    }
}
