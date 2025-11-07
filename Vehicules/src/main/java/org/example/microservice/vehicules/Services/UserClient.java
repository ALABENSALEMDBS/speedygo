package org.example.microservice.vehicules.Services;
import org.example.microservice.vehicules.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "user", configuration = FeignConfig.class)
public interface  UserClient { // <-- C'EST LA LE PROBLÈME
    @GetMapping("/api/users")
     List<UserDTO> getAllUsers(); // <-- PAS DE CORPS DANS UNE CLASSE CONCRÈTE
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") int id);
    @PutMapping("/api/users/{userId}/assignVehicle/{vehicleId}")
    void updateUserAssignedVehicle(@PathVariable("userId") Long userId, @PathVariable("vehicleId") String vehicleId);


}
