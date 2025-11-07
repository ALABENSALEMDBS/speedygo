package com.speedygo.fastpostdelivery.Controllers;

import com.speedygo.fastpostdelivery.Services.DeliveryFastPostService;
import com.speedygo.fastpostdelivery.entities.FastPost;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-fastpost")
@CrossOrigin(origins = "*")
public class DeliveryFastPostController {

    @Autowired
    private DeliveryFastPostService deliveryFastPostService;

    @PostConstruct
    public void init() {
        System.out.println("🚀 DeliveryFastPostController initialized!");
    }

    /**
     * Get the FastPost details for a delivery
     * @param deliveryId The ID of the delivery
     * @return The FastPost details or 404 if not found
     */
    @GetMapping("/{deliveryId}")
    public ResponseEntity<FastPost> getFastPostForDelivery(@PathVariable Long deliveryId) {
        System.out.println("📥 Received request for FastPost details for delivery ID: " + deliveryId);

        try {
            FastPost fastPost = deliveryFastPostService.getFastPostForDelivery(deliveryId);

            if (fastPost == null) {
                System.out.println("❌ No FastPost found for delivery ID: " + deliveryId);
                return ResponseEntity.notFound().build();
            }

            System.out.println("✅ Returning FastPost details: " + fastPost.getIdF() +
                              ", Receiver: " + fastPost.getReceiverName() +
                              ", Phone: " + fastPost.getReceiverTelNbr() +
                              ", Weight: " + fastPost.getPackageWeight());
            return ResponseEntity.ok(fastPost);
        } catch (Exception e) {
            System.out.println("❌ Error retrieving FastPost details: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Test endpoint to verify that the controller is accessible
     * @return Success message
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        System.out.println("📝 Test endpoint called");
        return ResponseEntity.ok("DeliveryFastPostController is working!");
    }
}

