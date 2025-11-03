package com.speedygo.fastpostdelivery.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idD;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime estimatedDeliveryTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String receiverAddress;

    // Constructeurs
    public Delivery() {}

    public Delivery(Long idD, DeliveryStatus deliveryStatus, LocalDateTime estimatedDeliveryTime, Status status, String receiverAddress) {
        this.idD = idD;
        this.deliveryStatus = deliveryStatus;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.status = status;
        this.receiverAddress = receiverAddress;
    }

    // Getters et Setters
    public Long getIdD() {
        return idD;
    }

    public void setIdD(Long idD) {
        this.idD = idD;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
}
