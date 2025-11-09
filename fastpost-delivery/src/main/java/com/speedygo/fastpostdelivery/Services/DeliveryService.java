package com.speedygo.fastpostdelivery.Services;

import com.speedygo.fastpostdelivery.Repositories.DeliveryRepository;
import com.speedygo.fastpostdelivery.client.UserClient;
import com.speedygo.fastpostdelivery.dto.UserDTO;
import com.speedygo.fastpostdelivery.entities.Delivery;
import com.speedygo.fastpostdelivery.entities.DeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private UserClient userClient;

    /**
     * Get all deliveries
     * @return List of all deliveries
     */
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    /**
     * Get a delivery by ID
     * @param id The delivery ID
     * @return The delivery or null if not found
     */
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    /**
     * Get all deliveries for a specific driver
     * @param driverId The driver ID
     * @return List of deliveries assigned to this driver
     */
    public List<Delivery> getDeliveriesByDriverId(Long driverId) {
        return deliveryRepository.findAll()
                .stream()
                .filter(delivery -> delivery.getDriverId() != null && delivery.getDriverId().equals(driverId))
                .collect(Collectors.toList());
    }

    /**
     * Get all deliveries for a specific client
     * @param userId The client user ID (from Keycloak)
     * @return List of deliveries created by this client
     */
    public List<Delivery> getDeliveriesByUserId(String userId) {
        return deliveryRepository.findAll()
                .stream()
                .filter(delivery -> delivery.getUserId() != null && delivery.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Save a delivery
     * @param delivery The delivery to save
     * @return The saved delivery
     */
    public Delivery saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    /**
     * Delete a delivery
     * @param id The delivery ID to delete
     */
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }

    /**
     * Accept a delivery by a driver
     * @param deliveryId The delivery ID
     * @param driverId The driver ID who is accepting
     * @return true if accepted successfully, false otherwise
     */
    public boolean acceptDelivery(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);

        if (delivery == null) {
            System.out.println("❌ Delivery not found: " + deliveryId);
            return false;
        }

        // Verify that this delivery is assigned to this driver
        if (delivery.getDriverId() == null || !delivery.getDriverId().equals(driverId)) {
            System.out.println("❌ Delivery " + deliveryId + " is not assigned to driver " + driverId);
            return false;
        }

        // Verify that the delivery is in PENDING status
        if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
            System.out.println("❌ Delivery " + deliveryId + " is not in PENDING status. Current status: " + delivery.getDeliveryStatus());
            return false;
        }

        // Accept the delivery
        delivery.setDeliveryStatus(DeliveryStatus.ACCEPTED);
        deliveryRepository.save(delivery);

        System.out.println("✅ Delivery " + deliveryId + " accepted by driver " + driverId +
                          " (" + delivery.getDriverFirstName() + " " + delivery.getDriverLastName() + ")");

        return true;
    }

    /**
     * Reject a delivery by a driver and reassign to another available driver
     * @param deliveryId The delivery ID
     * @param driverId The driver ID who is rejecting
     * @return true if rejected and reassigned successfully, false otherwise
     */
    public boolean rejectDelivery(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);

        if (delivery == null) {
            System.out.println("❌ Delivery not found: " + deliveryId);
            return false;
        }

        // Verify that this delivery is assigned to this driver
        if (delivery.getDriverId() == null || !delivery.getDriverId().equals(driverId)) {
            System.out.println("❌ Delivery " + deliveryId + " is not assigned to driver " + driverId);
            return false;
        }

        // Verify that the delivery is in PENDING status
        if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
            System.out.println("❌ Delivery " + deliveryId + " is not in PENDING status. Current status: " + delivery.getDeliveryStatus());
            return false;
        }

        System.out.println("🔄 Driver " + driverId + " rejected delivery " + deliveryId);

        // Decrement the current driver's daily delivery count
        try {
            UserDTO currentDriver = userClient.getUserById(driverId);
            if (currentDriver != null) {
                int newCount = Math.max(0, currentDriver.getDailyDeliveriesCount() - 1);
                userClient.updateDailyDeliveriesCount(driverId, newCount);
                System.out.println("✅ Decremented driver " + driverId + " daily count to: " + newCount);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not decrement driver count: " + e.getMessage());
        }

        // Find another available driver
        List<UserDTO> availableDrivers = userClient.getAllUsers()
                .stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains("driver"))
                .filter(UserDTO::isAvailable)
                .filter(user -> user.getDailyDeliveriesCount() < 5)
                .filter(user -> user.getAssignedVehicleId() != null && !user.getAssignedVehicleId().isEmpty())
                .filter(user -> !user.getId().equals(driverId)) // Exclude the driver who rejected
                .toList();

        if (availableDrivers.isEmpty()) {
            System.out.println("❌ No other available driver found. Delivery " + deliveryId + " will remain unassigned.");

            // Mark delivery as REJECTED and clear driver info
            delivery.setDeliveryStatus(DeliveryStatus.REJECTED);
            delivery.setDriverId(null);
            delivery.setDriverFirstName(null);
            delivery.setDriverLastName(null);
            deliveryRepository.save(delivery);

            return false;
        }

        // Assign to the first available driver
        UserDTO newDriver = availableDrivers.get(0);

        delivery.setDriverId(newDriver.getId());
        delivery.setDriverFirstName(newDriver.getFirstName());
        delivery.setDriverLastName(newDriver.getLastName());
        delivery.setDeliveryStatus(DeliveryStatus.PENDING); // Reset to PENDING for new driver
        deliveryRepository.save(delivery);

        // Increment the new driver's daily delivery count
        int newCount = newDriver.getDailyDeliveriesCount() + 1;
        userClient.updateDailyDeliveriesCount(newDriver.getId(), newCount);

        System.out.println("✅ Delivery " + deliveryId + " reassigned to driver " + newDriver.getId() +
                          " (" + newDriver.getFirstName() + " " + newDriver.getLastName() +
                          ") - Deliveries: " + newCount + "/5");

        return true;
    }

    /**
     * Get all deliveries for the current authenticated driver
     * @param driverId The driver ID
     * @return List of deliveries assigned to this driver
     */
    public List<Delivery> getMyDeliveries(Long driverId) {
        return deliveryRepository.findAll()
                .stream()
                .filter(delivery -> delivery.getDriverId() != null && delivery.getDriverId().equals(driverId))
                .filter(delivery -> delivery.getDeliveryStatus() == DeliveryStatus.PENDING ||
                                   delivery.getDeliveryStatus() == DeliveryStatus.ACCEPTED ||
                                   delivery.getDeliveryStatus() == DeliveryStatus.IN_PROGRESS)
                .collect(Collectors.toList());
    }
}

