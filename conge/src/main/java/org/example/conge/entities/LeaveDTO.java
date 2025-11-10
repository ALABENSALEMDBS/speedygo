package org.example.conge.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.conge.entities.Status;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDTO {
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private Status status;
    private String driverId;
    private String driverFirstName;
    private String driverLastName;
    private String driverFullName;

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

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public String getDriverFullName() {
        return driverFullName;
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

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }
}
