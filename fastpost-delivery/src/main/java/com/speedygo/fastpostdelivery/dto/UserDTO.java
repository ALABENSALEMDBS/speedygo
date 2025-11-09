package com.speedygo.fastpostdelivery.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String assignedVehicleId;
    private List<String> roles;
    private boolean available;
    private int dailyDeliveriesCount;
    private String currentDeliveryAddress;

    // Constructors
    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String email, String assignedVehicleId, 
                   List<String> roles, boolean available, int dailyDeliveriesCount, String currentDeliveryAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.assignedVehicleId = assignedVehicleId;
        this.roles = roles;
        this.available = available;
        this.dailyDeliveriesCount = dailyDeliveriesCount;
        this.currentDeliveryAddress = currentDeliveryAddress;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssignedVehicleId() {
        return assignedVehicleId;
    }

    public void setAssignedVehicleId(String assignedVehicleId) {
        this.assignedVehicleId = assignedVehicleId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getDailyDeliveriesCount() {
        return dailyDeliveriesCount;
    }

    public void setDailyDeliveriesCount(int dailyDeliveriesCount) {
        this.dailyDeliveriesCount = dailyDeliveriesCount;
    }

    public String getCurrentDeliveryAddress() {
        return currentDeliveryAddress;
    }

    public void setCurrentDeliveryAddress(String currentDeliveryAddress) {
        this.currentDeliveryAddress = currentDeliveryAddress;
    }
}

