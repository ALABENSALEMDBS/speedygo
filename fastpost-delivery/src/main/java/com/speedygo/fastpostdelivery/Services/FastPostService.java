package com.speedygo.fastpostdelivery.Services;

import com.speedygo.fastpostdelivery.Repositories.DeliveryRepository;
import com.speedygo.fastpostdelivery.Repositories.FastPostRepository;
import com.speedygo.fastpostdelivery.client.UserClient;
import com.speedygo.fastpostdelivery.dto.UserDTO;
import com.speedygo.fastpostdelivery.entities.Delivery;
import com.speedygo.fastpostdelivery.entities.DeliveryStatus;
import com.speedygo.fastpostdelivery.entities.FastPost;
import com.speedygo.fastpostdelivery.entities.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class FastPostService {
    private final FastPostRepository fastPostRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserClient userClient;

    public FastPostService(FastPostRepository fastPostRepository, DeliveryRepository deliveryRepository, UserClient userClient) {
        this.fastPostRepository = fastPostRepository;
        this.deliveryRepository = deliveryRepository;
        this.userClient = userClient;
    }

    public FastPost addFastPost(FastPost fastPost) {
        fastPost.setFastPostStatus(Status.PENDING);
        return fastPostRepository.save(fastPost);
    }

    public void deleteFastPost(Long idF) {
        fastPostRepository.deleteById(idF);
    }

    public FastPost modifyFastPost(Long idF, FastPost updatedFastPost) {
        return fastPostRepository.findById(idF).map(fastPost -> {
            fastPost.setReceiverName(updatedFastPost.getReceiverName());
            fastPost.setReceiverAddress(updatedFastPost.getReceiverAddress());
            fastPost.setReceiverTelNbr(updatedFastPost.getReceiverTelNbr());
            fastPost.setPackageWeight(updatedFastPost.getPackageWeight());
            fastPost.setFastPostStatus(updatedFastPost.getFastPostStatus());
            return fastPostRepository.save(fastPost);
        }).orElseThrow(() -> new RuntimeException("❌ FastPost not found with ID: " + idF));
    }

    public List<FastPost> getAllFastPosts() {
        return fastPostRepository.findAll();
    }

    public FastPost getFastPost(Long idF) {
        return fastPostRepository.findById(idF).orElse(null);
    }

    public void approveFastPost(Long idF) {
        fastPostRepository.findById(idF).ifPresent(fastPost -> {
            fastPost.setFastPostStatus(Status.APPROVED);
            fastPost.setAssignedToDelivery(true);
            fastPostRepository.save(fastPost);

            System.out.println("✅ FastPost approved: " + idF);

            // Find an available driver automatically
            List<UserDTO> availableDrivers = userClient.getAllUsers()
                    .stream()
                    .filter(user -> user.getRoles() != null && user.getRoles().contains("driver"))
                    .filter(UserDTO::isAvailable)
                    .filter(user -> user.getDailyDeliveriesCount() < 5)
                    .filter(user -> user.getAssignedVehicleId() != null && !user.getAssignedVehicleId().isEmpty())
                    .toList();

            if (availableDrivers.isEmpty()) {
                System.out.println("❌ No available driver found for FastPost: " + idF);
                // Create delivery without driver assignment
                Delivery delivery = new Delivery();
                delivery.setDeliveryStatus(DeliveryStatus.PENDING);
                delivery.setStatus(Status.PENDING);

                // Copy FastPost details to Delivery
                delivery.setFastPostId(fastPost.getIdF());
                delivery.setReceiverName(fastPost.getReceiverName());
                delivery.setReceiverAddress(fastPost.getReceiverAddress());
                delivery.setReceiverTelNbr(fastPost.getReceiverTelNbr());
                delivery.setPackageWeight(fastPost.getPackageWeight());
                delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(2));

                // Set client information
                delivery.setUserId(fastPost.getUserId());
                delivery.setClientFirstName(fastPost.getClientFirstName());
                delivery.setClientLastName(fastPost.getClientLastName());

                deliveryRepository.save(delivery);
                return;
            }

            // Get the first available driver
            UserDTO driver = availableDrivers.get(0);

            // Create delivery with driver assignment
            Delivery delivery = new Delivery();
            delivery.setDeliveryStatus(DeliveryStatus.PENDING);
            delivery.setStatus(Status.PENDING);

            // Copy FastPost details to Delivery (for driver to see)
            delivery.setFastPostId(fastPost.getIdF());
            delivery.setReceiverName(fastPost.getReceiverName());
            delivery.setReceiverAddress(fastPost.getReceiverAddress());
            delivery.setReceiverTelNbr(fastPost.getReceiverTelNbr());
            delivery.setPackageWeight(fastPost.getPackageWeight());
            delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(2));

            // Set client information from FastPost
            delivery.setUserId(fastPost.getUserId());
            delivery.setClientFirstName(fastPost.getClientFirstName());
            delivery.setClientLastName(fastPost.getClientLastName());

            // Assign driver to the delivery
            delivery.setDriverId(driver.getId());
            delivery.setDriverFirstName(driver.getFirstName());
            delivery.setDriverLastName(driver.getLastName());

            deliveryRepository.save(delivery);

            // Increment the driver's daily delivery count
            int newCount = driver.getDailyDeliveriesCount() + 1;
            userClient.updateDailyDeliveriesCount(driver.getId(), newCount);

            System.out.println("✅ FastPost " + idF + " approved and assigned to driver: " +
                              driver.getFirstName() + " " + driver.getLastName() +
                              " (Deliveries: " + newCount + "/5)");
        });
    }

    public void rejectFastPost(Long idF) {
        fastPostRepository.findById(idF).ifPresent(fastPost -> {
            fastPost.setFastPostStatus(Status.REJECTED);
            fastPostRepository.save(fastPost);
        });
    }

    public List<FastPost> getApprovedFastPosts() {
        return fastPostRepository.findByFastPostStatus(Status.APPROVED);
    }
}
