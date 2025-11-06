package com.esprit.microservice.covoiturage.Services;


import com.esprit.microservice.covoiturage.Repository.UserClient;
import com.esprit.microservice.covoiturage.entities.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.esprit.microservice.covoiturage.Repository.CarpoolingRepository;
import com.esprit.microservice.covoiturage.entities.Carpooling;
import com.esprit.microservice.covoiturage.entities.Services;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CarpoolingService {

    @Autowired
    private CarpoolingRepository carpoolingRepository;
    @Autowired
    private UserClient userClient;
//    public UserDTO getUserById(Long id) {
//        return userClient.getUserById(id);
//    }


    public List<Carpooling> getAllRides() {
        return carpoolingRepository.findAll();
    }

    public Optional<Carpooling> getRideById(String id) {
        return carpoolingRepository.findById(id);
    }

//    public Carpooling createRide(Carpooling carpooling) {
//        // Set type automatically based on rental flag
//        if (carpooling.isRental()) {
//            carpooling.setTypeservice(Services.RENTAL);
//            carpooling.setPricePerSeat(0); // optional: rental rides use total price
//        } else {
//            carpooling.setTypeservice(Services.Carpooling);
//        }
//        return carpoolingRepository.save(carpooling);
//    }

    public CarpoolingService(CarpoolingRepository carpoolingRepository) {
        this.carpoolingRepository = carpoolingRepository;
    }

    // 🔹 Récupère l’ID de l’utilisateur connecté à partir du token Keycloak
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("sub"); // c’est l’ID Keycloak unique
    }

    // 🔹 Récupère le nom d’utilisateur Keycloak
//    private String getCurrentUsername() {
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return jwt.getClaimAsString("preferred_username");
//    }
    private String getCurrentFirstName() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("given_name"); // prénom Keycloak
    }


    public Carpooling createCarpooling(Carpooling carpoolingRequest) {
        Carpooling carpooling = new Carpooling();

        // 🔹 Copie des données reçues du frontend
        carpooling.setPickupLocation(carpoolingRequest.getPickupLocation());
        carpooling.setDropoffLocation(carpoolingRequest.getDropoffLocation());
        carpooling.setDepartureTime(carpoolingRequest.getDepartureTime());
        carpooling.setAvailableSeats(carpoolingRequest.getAvailableSeats());
        carpooling.setPricePerSeat(carpoolingRequest.getPricePerSeat());
        carpooling.setTypeservice(carpoolingRequest.getTypeservice());
        carpooling.setRental(carpoolingRequest.isRental());
        carpooling.setRentalDurationHours(carpoolingRequest.getRentalDurationHours());
        carpooling.setTotalRentalPrice(carpoolingRequest.getTotalRentalPrice());
        carpooling.setOccasion(carpoolingRequest.getOccasion());

        // 🔹 Ajoute les infos du driver connecté
        carpooling.setDriverId(getCurrentUserId());
        carpooling.setDriverName(getCurrentFirstName());

        // 🔹 Sauvegarde en base
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

