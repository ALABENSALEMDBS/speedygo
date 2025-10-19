package org.example.conge.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "leave_statistics")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeaveStatistics {
    @Id
    private String id;
    private int totalDaysTaken;
}
