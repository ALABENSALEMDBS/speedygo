package com.ticketplatform.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String zipCode;

    @Column(nullable = false)
    private Boolean active = true;

    @ElementCollection
    private List<String> roles= new ArrayList<>();;
    @Column(nullable = true)
    private String assignedVehicleId;

    // Driver availability fields for delivery assignment
//    @Column(nullable = false)
//    private boolean available = true;

    @Column(nullable = false)
    private int dailyDeliveriesCount = 0; // nombre de livraisons quotidiennes

    @Column(nullable = true)
    private String currentDeliveryAddress; // adresse des livraisons actuelles du chauffeur
}

