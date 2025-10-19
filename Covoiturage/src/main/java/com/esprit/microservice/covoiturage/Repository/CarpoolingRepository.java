package com.esprit.microservice.covoiturage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.esprit.microservice.covoiturage.entities.Carpooling;
import com.esprit.microservice.covoiturage.entities.Services;


import java.util.List;

@Repository
public interface CarpoolingRepository extends JpaRepository<Carpooling, String> {
    List<Carpooling> findByPickupLocationAndDropoffLocation(String pickup, String dropoff);
    List<Carpooling> findByTypeservice(Services typeservice);
    List<Carpooling> findByIsRentalTrue();
    List<Carpooling> findByTotalRentalPriceGreaterThan(double price);

    List<Carpooling> findByDriverId(String driverId);


}
