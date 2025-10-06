package org.example.microservice.vehicules.Repository;

import org.example.microservice.vehicules.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    @Query("SELECT v FROM Vehicle v WHERE v.brand LIKE %:brand%")
    List<Vehicle> findByBrand(String brand);

    // Trouver les véhicules déjà assignés à un livreur - using relationship navigation
//    List<Vehicle> findByDriver_Id(String driverId);

    // Alternative using custom query
//    @Query("SELECT v FROM Vehicle v WHERE v.driver.id = :driverId")
//    List<Vehicle> findByAssignedToDriverId(@Param("driverId") String driverId);

}
