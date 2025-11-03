package com.esprit.microservice.covoiturage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.esprit.microservice.covoiturage.entities.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByRideId(String rideId);
    List<Booking> findByPassengerId(String passengerId);

}
