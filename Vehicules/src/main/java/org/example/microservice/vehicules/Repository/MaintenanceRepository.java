package org.example.microservice.vehicules.Repository;

import org.example.microservice.vehicules.entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, String> {
    // ⚠️ Toutes les maintenances pas encore faites
    List<Maintenance> findByCompletedFalse();

    // ⚠️ Celles à faire (trigger atteint + pas encore complétées)
    List<Maintenance> findByTriggerDistanceKmLessThanEqualAndCompletedIsFalse(double currentKm);

    // 🔎 Pour un véhicule donné - using relationship navigation
    List<Maintenance> findByVehicle_IdV(String vehicleId);

    // Alternative using custom query
    @Query("SELECT m FROM Maintenance m WHERE m.vehicle.idV = :vehicleId")
    List<Maintenance> findByVehicleId(@Param("vehicleId") String vehicleId);
}
