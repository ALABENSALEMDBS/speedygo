package org.example.conge.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "leave_settings")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class LeaveSettings {
    @Id
    private String id;
    private int maxAllowedDays;

    public void setMaxAllowedDays(int maxAllowedDays) {
        this.maxAllowedDays = maxAllowedDays;
    }

}
