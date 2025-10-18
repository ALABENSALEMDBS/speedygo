package com.speedygo.fastpostdelivery.Services;

import com.speedygo.fastpostdelivery.Repositories.DeliveryRepository;
import com.speedygo.fastpostdelivery.Repositories.FastPostRepository;
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

    public FastPostService(FastPostRepository fastPostRepository, DeliveryRepository deliveryRepository) {
        this.fastPostRepository = fastPostRepository;
        this.deliveryRepository = deliveryRepository;
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

            Delivery delivery = new Delivery();
            delivery.setDeliveryStatus(DeliveryStatus.PENDING);
            delivery.setStatus(Status.PENDING);
            delivery.setReceiverAddress(fastPost.getReceiverAddress());
            delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(2));

            deliveryRepository.save(delivery);
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
