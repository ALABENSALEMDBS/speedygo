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
}
