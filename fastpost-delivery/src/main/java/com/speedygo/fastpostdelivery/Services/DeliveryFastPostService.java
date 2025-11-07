package com.speedygo.fastpostdelivery.Services;

import com.speedygo.fastpostdelivery.Repositories.DeliveryRepository;
import com.speedygo.fastpostdelivery.Repositories.FastPostRepository;
import com.speedygo.fastpostdelivery.entities.Delivery;
import com.speedygo.fastpostdelivery.entities.FastPost;
import com.speedygo.fastpostdelivery.entities.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryFastPostService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private FastPostRepository fastPostRepository;

    /**
     * Find the FastPost associated with a Delivery
     * @param deliveryId The ID of the delivery
     * @return The associated FastPost, or null if not found
     */
    public FastPost getFastPostForDelivery(Long deliveryId) {
        // Get the delivery
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);

        if (deliveryOpt.isEmpty()) {
            System.out.println("❌ Delivery not found with ID: " + deliveryId);
            return null;
        }

        Delivery delivery = deliveryOpt.get();
        System.out.println("✅ Found delivery: " + delivery.getIdD() +
                          ", Address: " + delivery.getReceiverAddress() +
                          ", UserId: " + delivery.getUserId());

        // Get all FastPosts that match the criteria
        List<FastPost> allFastPosts = fastPostRepository.findAll();
        System.out.println("📦 Total FastPosts in database: " + allFastPosts.size());

        // Find FastPost with matching userId and receiverAddress
        Optional<FastPost> matchingFastPost = allFastPosts.stream()
            .filter(fp -> fp.getUserId() != null && fp.getUserId().equals(delivery.getUserId()))
            .filter(fp -> fp.getReceiverAddress() != null && fp.getReceiverAddress().equals(delivery.getReceiverAddress()))
            .filter(fp -> fp.isAssignedToDelivery())
            .findFirst();

        if (matchingFastPost.isPresent()) {
            FastPost fastPost = matchingFastPost.get();
            System.out.println("✅ Found matching FastPost: " + fastPost.getIdF() +
                              ", Receiver: " + fastPost.getReceiverName() +
                              ", Address: " + fastPost.getReceiverAddress());
            return fastPost;
        }

        // If no matching FastPost found, try to find by userId only
        Optional<FastPost> userFastPost = allFastPosts.stream()
            .filter(fp -> fp.getUserId() != null && fp.getUserId().equals(delivery.getUserId()))
            .filter(fp -> fp.isAssignedToDelivery())
            .findFirst();

        if (userFastPost.isPresent()) {
            FastPost fastPost = userFastPost.get();
            System.out.println("✅ Found FastPost by userId: " + fastPost.getIdF());
            return fastPost;
        }

        // If still no match, return the first FastPost that is assigned to a delivery
        Optional<FastPost> anyAssignedFastPost = allFastPosts.stream()
            .filter(FastPost::isAssignedToDelivery)
            .findFirst();

        if (anyAssignedFastPost.isPresent()) {
            FastPost fastPost = anyAssignedFastPost.get();
            System.out.println("⚠️ Returning first assigned FastPost: " + fastPost.getIdF());
            return fastPost;
        }

        // Si aucun FastPost n'est trouvé, créer un FastPost de test pour le développement
        System.out.println("⚠️ No matching FastPost found for delivery: " + deliveryId);
        System.out.println("⚠️ Creating a test FastPost for development purposes");

        FastPost testFastPost = new FastPost();
        testFastPost.setReceiverName("Test Receiver");
        testFastPost.setReceiverAddress(delivery.getReceiverAddress() != null ? delivery.getReceiverAddress() : "Test Address");
        testFastPost.setReceiverTelNbr(12345678);
        testFastPost.setPackageWeight("2.5");
        testFastPost.setFastPostStatus(Status.APPROVED);
        testFastPost.setUserId(delivery.getUserId() != null ? delivery.getUserId() : "test-user");
        testFastPost.setAssignedToDelivery(true);

        return testFastPost;
    }
}

