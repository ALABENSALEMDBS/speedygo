package org.example.conge.client;

import org.example.conge.config.FeignClientConfig;
import org.example.conge.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/api/users")
    List<UserDTO> getAllUsers();

    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

    @PutMapping("/api/users/{id}/dailyDeliveries")
    void updateDailyDeliveriesCount(@PathVariable("id") Long id, @RequestParam("count") int count);
}

