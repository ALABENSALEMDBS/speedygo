package org.example.conge.entities;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int maxAllowedDays;

    // Getters
    public Long getId() {
        return id;
    }

    public int getMaxAllowedDays() {
        return maxAllowedDays;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaxAllowedDays(int maxAllowedDays) {
        this.maxAllowedDays = maxAllowedDays;
    }
}
