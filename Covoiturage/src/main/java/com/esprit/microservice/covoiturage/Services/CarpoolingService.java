package com.esprit.microservice.covoiturage.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.esprit.microservice.covoiturage.Repository.CarpoolingRepository;
import com.esprit.microservice.covoiturage.entities.Carpooling;
import com.esprit.microservice.covoiturage.entities.Services;

import java.util.List;
import java.util.Optional;

@Service
public class CarpoolingService {

    @Autowired
    private CarpoolingRepository carpoolingRepository;

    public List<Carpooling> getAllRides() {
        return carpoolingRepository.findAll();
    }

    public Optional<Carpooling> getRideById(String id) {
        return carpoolingRepository.findById(id);
    }

    public Carpooling createRide(Carpooling carpooling) {
        // Set type automatically based on rental flag
        if (carpooling.isRental()) {
            carpooling.setTypeservice(Services.RENTAL);
            carpooling.setPricePerSeat(0); // optional: rental rides use total price
        } else {
            carpooling.setTypeservice(Services.Carpooling);
        }
        return carpoolingRepository.save(carpooling);
    }

    public Carpooling updateRide(String id, Carpooling updatedCarpooling) {
        return carpoolingRepository.findById(id).map(ride -> {
            ride.setDriverName(updatedCarpooling.getDriverName());
            ride.setPickupLocation(updatedCarpooling.getPickupLocation());
            ride.setDropoffLocation(updatedCarpooling.getDropoffLocation());
            ride.setDepartureTime(updatedCarpooling.getDepartureTime());
            ride.setAvailableSeats(updatedCarpooling.getAvailableSeats());
            ride.setPricePerSeat(updatedCarpooling.getPricePerSeat());

            ride.setRental(updatedCarpooling.isRental());
            ride.setRentalDurationHours(updatedCarpooling.getRentalDurationHours());
            ride.setTotalRentalPrice(updatedCarpooling.getTotalRentalPrice());
            ride.setOccasion(updatedCarpooling.getOccasion());

            // Auto-assign service type
            ride.setTypeservice(updatedCarpooling.isRental() ? Services.RENTAL : Services.Carpooling);

            return carpoolingRepository.save(ride);
        }).orElse(null);
    }

    public void deleteRide(String id) {
        carpoolingRepository.deleteById(id);
    }

    // 🔹 Get all rental rides
    public List<Carpooling> getRentalRides() {
        return carpoolingRepository.findByIsRentalTrue();
    }

    // 🔹 Get rides where totalRentalPrice > X (long-distance or special trips)
    public List<Carpooling> getLongDistanceRides(double minPrice) {
        return carpoolingRepository.findByTotalRentalPriceGreaterThan(minPrice);
    }

    public List<Carpooling> getRidesByDriverId(String driverId) {
        return carpoolingRepository.findByDriverId(driverId);
    }


}

