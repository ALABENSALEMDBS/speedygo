package com.esprit.microservice.covoiturage.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.esprit.microservice.covoiturage.Repository.CarpoolingRepository;
import com.esprit.microservice.covoiturage.Services.CarpoolingService;
import com.esprit.microservice.covoiturage.entities.Carpooling;
import com.esprit.microservice.covoiturage.entities.Services;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carpooling")
public class CarpoolingController {

    @Autowired
    private CarpoolingService carpoolingService;

    @GetMapping(value = "/getAllRides")
    public List<Carpooling> getAllRides() {
        return carpoolingService.getAllRides();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carpooling> getRideById(@PathVariable String id) {
        Optional<Carpooling> ride = carpoolingService.getRideById(id);
        return ride.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createRide")
    public Carpooling createRide(@RequestBody Carpooling carpooling) {
        return carpoolingService.createRide(carpooling);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carpooling> updateRide(@PathVariable String id, @RequestBody Carpooling updatedCarpooling) {
        Carpooling ride = carpoolingService.updateRide(id, updatedCarpooling);
        return ride != null ? ResponseEntity.ok(ride) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable String id) {
        carpoolingService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/rentals")
    public List<Carpooling> getRentalRides() {
        return carpoolingService.getRentalRides();
    }

    // ✅ Get all long-distance rides (above given totalRentalPrice)
    @GetMapping("/long-distance")
    public List<Carpooling> getLongDistanceRides(@RequestParam(defaultValue = "100") double minPrice) {
        return carpoolingService.getLongDistanceRides(minPrice);
    }


    @GetMapping("/by-driver/{driverId}")
    public List<Carpooling> getRidesByDriver(@PathVariable String driverId) {
        return carpoolingService.getRidesByDriverId(driverId);
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }


}

