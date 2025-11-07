package com.speedygo.fastpostdelivery.entities;

import jakarta.persistence.*;

@Entity
public class FastPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idF;

    private String receiverName;
    private String receiverAddress;
    private int receiverTelNbr;
    private String packageWeight;

    @Enumerated(EnumType.STRING)
    private Status fastPostStatus;

    private boolean assignedToDelivery = false;

    // User tracking fields
    private String userId; // ID of the user who created the FastPost
    private String clientFirstName; // First name of the client
    private String clientLastName; // Last name of the client

    // Constructeurs
    public FastPost() {}

    public FastPost(Long idF, String receiverName, String receiverAddress, int receiverTelNbr, String packageWeight, Status fastPostStatus, boolean assignedToDelivery) {
        this.idF = idF;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverTelNbr = receiverTelNbr;
        this.packageWeight = packageWeight;
        this.fastPostStatus = fastPostStatus;
        this.assignedToDelivery = assignedToDelivery;
    }

    // Getters et Setters
    public Long getIdF() {
        return idF;
    }

    public void setIdF(Long idF) {
        this.idF = idF;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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

    public Status getFastPostStatus() {
        return fastPostStatus;
    }

    public void setFastPostStatus(Status fastPostStatus) {
        this.fastPostStatus = fastPostStatus;
    }

    public boolean isAssignedToDelivery() {
        return assignedToDelivery;
    }

    public void setAssignedToDelivery(boolean assignedToDelivery) {
        this.assignedToDelivery = assignedToDelivery;
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
}
