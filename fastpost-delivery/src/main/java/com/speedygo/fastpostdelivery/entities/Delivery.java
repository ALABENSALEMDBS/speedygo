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

    // FastPost details (for driver to see delivery information)
    private String receiverName;        // Nom du destinataire
    private String receiverAddress;     // Adresse de livraison
    private int receiverTelNbr;         // Numéro de téléphone du destinataire
    private String packageWeight;       // Poids du colis
    private Long fastPostId;            // ID du FastPost original

    // Client information
    private String userId; // ID of the client who created the FastPost
    private String clientFirstName;
    private String clientLastName;

    // Driver information
    private Long driverId; // ID of the driver assigned to this delivery
    private String driverFirstName;
    private String driverLastName;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getReceiverTelNbr() {
        return receiverTelNbr;
    }

    public void setReceiverTelNbr(int receiverTelNbr) {
        this.receiverTelNbr = receiverTelNbr;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public Long getFastPostId() {
        return fastPostId;
    }

    public void setFastPostId(Long fastPostId) {
        this.fastPostId = fastPostId;
    }
}
