package com.esprit.microservice.covoiturage.entities;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carpooling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carpooling {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String driverId;
    private String driverName;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime departureTime;
    private int availableSeats;
    private double pricePerSeat;

    @Enumerated(EnumType.STRING) // <-- stocke le texte de l'enum
    private Services typeservice;

    private boolean isRental; // true pour les locations
    private int rentalDurationHours;
    private double totalRentalPrice;
    private String occasion;
}
