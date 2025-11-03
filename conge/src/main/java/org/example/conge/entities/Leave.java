package org.example.conge.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "leaves")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Leave {

    @Id
    private String id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String driverId;
    private int exceededDays;
    private double exceededSalaryCut;


    public Status getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getExceededSalaryCut() {
        return exceededSalaryCut;
    }

    // Ajoutez d'autres getters si nécessaire
    public String getDriverId() {
        return driverId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getExceededDays() {
        return exceededDays;
    }
}
