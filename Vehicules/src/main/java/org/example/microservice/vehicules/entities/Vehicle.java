package org.example.microservice.vehicules.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private String idV; // Keeping String ID for backward compatibility

    private String brand;
    private String model;
    private String capacity;

    @Column(unique = true)
    private String licensePlate;

    @Column(unique = true)
    private String vin;

    @Temporal(TemporalType.DATE)
    private Date fabricationDate;

    private String fuelType;
    @Column(length = 100000)
    private String imageFileName;

    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    @Enumerated(EnumType.STRING)
    private Status vehicleStatusD;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String adminName;
    private boolean available = true; // pour l'auto-assignation



//    @Column(name = "assigned_driver_id", insertable = false, updatable = false)
    private String assignedToDriverId; // ID du livreur assigné - keeping for backward compatibility - read-only
    private String driverFirstName;
    private String driverLastName;

    private double currentDistanceKm = 0; // Current mileage of the vehicle
    private double maintenanceThresholdKm = 10000; // Maintenance threshold in kilometers

    // Relationship with Maintenance
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Maintenance> maintenanceRecords = new ArrayList<>();



}
