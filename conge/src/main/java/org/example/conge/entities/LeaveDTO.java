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
}
