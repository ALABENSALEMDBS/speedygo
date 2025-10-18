package com.esprit.microservice.complaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(length = 1000)
    private String description;

    // Creation timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Enumerated(EnumType.STRING)
    private ComplaintCategory category;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status;


    // Contact information
    private String contactEmail;
    private String contactPhone;


    // User who created the complaint (for permission control)
    private int userId;



}
