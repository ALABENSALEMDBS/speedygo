package com.esprit.microservice.covoiturage.entities;


import java.time.LocalDateTime;

import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    private String id;

    private String rideId;
    private String passengerId;
    private int seatsBooked;
    private String status; // PENDING / CONFIRMED / CANCELED
}

