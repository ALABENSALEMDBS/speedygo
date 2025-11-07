package com.speedygo.fastpostdelivery.Controllers;

import com.speedygo.fastpostdelivery.Services.DeliveryService;
import com.speedygo.fastpostdelivery.entities.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    /**
     * Get all deliveries
     * @return List of all deliveries with client and driver information
     */
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        System.out.println("📦 Getting all deliveries");
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        System.out.println("✅ Found " + deliveries.size() + " deliveries");
        return ResponseEntity.ok(deliveries);
    }

    /**
     * Get a specific delivery by ID
     * @param id The delivery ID
     * @return The delivery details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        System.out.println("📦 Getting delivery with ID: " + id);
        Delivery delivery = deliveryService.getDeliveryById(id);
        
        if (delivery == null) {
            System.out.println("❌ Delivery not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("✅ Found delivery: " + id);
        return ResponseEntity.ok(delivery);
    }

    /**
     * Get all deliveries for a specific driver
     * @param driverId The driver ID
     * @return List of deliveries assigned to this driver
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Delivery>> getDeliveriesByDriver(@PathVariable Long driverId) {
        System.out.println("🚗 Getting deliveries for driver ID: " + driverId);
        List<Delivery> deliveries = deliveryService.getDeliveriesByDriverId(driverId);
        System.out.println("✅ Found " + deliveries.size() + " deliveries for driver " + driverId);
        return ResponseEntity.ok(deliveries);
    }

    /**
     * Get all deliveries for a specific client
     * @param userId The client user ID (from Keycloak)
     * @return List of deliveries created by this client
     */
    @GetMapping("/client/{userId}")
    public ResponseEntity<List<Delivery>> getDeliveriesByClient(@PathVariable String userId) {
        System.out.println("👤 Getting deliveries for client ID: " + userId);
        List<Delivery> deliveries = deliveryService.getDeliveriesByUserId(userId);
        System.out.println("✅ Found " + deliveries.size() + " deliveries for client " + userId);
        return ResponseEntity.ok(deliveries);
    }

    /**
     * Get all deliveries for the authenticated driver (my deliveries)
     * @param driverId The driver ID
     * @return List of deliveries assigned to this driver
     */
    @GetMapping("/my-deliveries/{driverId}")
    public ResponseEntity<List<Delivery>> getMyDeliveries(@PathVariable Long driverId) {
        System.out.println("🚗 Getting my deliveries for driver ID: " + driverId);
        List<Delivery> deliveries = deliveryService.getMyDeliveries(driverId);
        System.out.println("✅ Found " + deliveries.size() + " active deliveries for driver " + driverId);
        return ResponseEntity.ok(deliveries);
    }

    /**
     * Accept a delivery by a driver
     * @param deliveryId The delivery ID to accept
     * @param driverId The driver ID who is accepting
     * @return Success or error message
     */
    @PutMapping("/{deliveryId}/accept/{driverId}")
    public ResponseEntity<String> acceptDelivery(@PathVariable Long deliveryId, @PathVariable Long driverId) {
        System.out.println("✅ Driver " + driverId + " attempting to accept delivery " + deliveryId);

        boolean success = deliveryService.acceptDelivery(deliveryId, driverId);

        if (success) {
            return ResponseEntity.ok("Delivery " + deliveryId + " accepted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to accept delivery " + deliveryId +
                    ". Please check if the delivery is assigned to you and is in PENDING status.");
        }
    }

    /**
     * Reject a delivery by a driver (will be reassigned to another driver)
     * @param deliveryId The delivery ID to reject
     * @param driverId The driver ID who is rejecting
     * @return Success or error message
     */
    @PutMapping("/{deliveryId}/reject/{driverId}")
    public ResponseEntity<String> rejectDelivery(@PathVariable Long deliveryId, @PathVariable Long driverId) {
        System.out.println("❌ Driver " + driverId + " attempting to reject delivery " + deliveryId);

        boolean success = deliveryService.rejectDelivery(deliveryId, driverId);

        if (success) {
            return ResponseEntity.ok("Delivery " + deliveryId + " rejected and reassigned to another driver successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to reject delivery " + deliveryId +
                    ". The delivery could not be reassigned (no available drivers).");
        }
    }

    /**
     * Update delivery status to IN_PROGRESS (driver started delivery)
     * @param deliveryId The delivery ID
     * @param driverId The driver ID
     * @return Success or error message
     */
    @PutMapping("/{deliveryId}/start/{driverId}")
    public ResponseEntity<String> startDelivery(@PathVariable Long deliveryId, @PathVariable Long driverId) {
        System.out.println("🚚 Driver " + driverId + " starting delivery " + deliveryId);

        Delivery delivery = deliveryService.getDeliveryById(deliveryId);

        if (delivery == null) {
            return ResponseEntity.notFound().build();
        }

        if (!delivery.getDriverId().equals(driverId)) {
            return ResponseEntity.badRequest().body("This delivery is not assigned to you.");
        }

        delivery.setDeliveryStatus(com.speedygo.fastpostdelivery.entities.DeliveryStatus.IN_PROGRESS);
        deliveryService.saveDelivery(delivery);

        System.out.println("✅ Delivery " + deliveryId + " is now IN_PROGRESS");
        return ResponseEntity.ok("Delivery started successfully!");
    }

    /**
     * Mark delivery as DELIVERED (completed)
     * @param deliveryId The delivery ID
     * @param driverId The driver ID
     * @return Success or error message
     */
    @PutMapping("/{deliveryId}/complete/{driverId}")
    public ResponseEntity<String> completeDelivery(@PathVariable Long deliveryId, @PathVariable Long driverId) {
        System.out.println("✅ Driver " + driverId + " completing delivery " + deliveryId);

        Delivery delivery = deliveryService.getDeliveryById(deliveryId);

        if (delivery == null) {
            return ResponseEntity.notFound().build();
        }

        if (!delivery.getDriverId().equals(driverId)) {
            return ResponseEntity.badRequest().body("This delivery is not assigned to you.");
        }

        delivery.setDeliveryStatus(com.speedygo.fastpostdelivery.entities.DeliveryStatus.DELIVERED);
        deliveryService.saveDelivery(delivery);

        System.out.println("🎉 Delivery " + deliveryId + " marked as DELIVERED");
        return ResponseEntity.ok("Delivery completed successfully!");
    }
}

