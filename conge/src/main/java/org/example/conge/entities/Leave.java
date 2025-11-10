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


    // Getters
    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public Status getStatus() {
        return status;
    }

    public String getDriverId() {
        return driverId;
    }

    public int getExceededDays() {
        return exceededDays;
    }

    public double getExceededSalaryCut() {
        return exceededSalaryCut;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setExceededDays(int exceededDays) {
        this.exceededDays = exceededDays;
    }

    public void setExceededSalaryCut(double exceededSalaryCut) {
        this.exceededSalaryCut = exceededSalaryCut;
    }
}
