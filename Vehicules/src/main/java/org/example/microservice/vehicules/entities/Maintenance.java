package org.example.microservice.vehicules.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.persistence.*;

@Schema(description = "Entité de maintenance véhicule")
@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Maintenance {
    @Id
    private String id;

    // Relationship with Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @ToString.Exclude
    private Vehicle vehicle;

    // Helper method to get vehicleId for backward compatibility
    public String getVehicleId() {
        return vehicle != null ? vehicle.getIdV() : null;
    }

    // Helper method to set vehicleId for backward compatibility
    public void setVehicleId(String vehicleId) {
        // This will be handled through the vehicle relationship
        // Services should use setVehicle() instead
    }

    @Column(length = 500)
    private String description;          // ex: "Vidange", "Révision 10k km"

    private double triggerDistanceKm;    // déclenché à quel kilométrage (ex: 10000 km)
    private double currentDistanceKm;    // kilométrage au moment de la maintenance

    private boolean completed = false;   // statut effectué ou non

}
