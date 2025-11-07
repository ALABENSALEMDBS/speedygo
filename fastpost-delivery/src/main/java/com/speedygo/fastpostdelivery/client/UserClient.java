package com.speedygo.fastpostdelivery.client;

import com.speedygo.fastpostdelivery.config.FeignClientConfig;
import com.speedygo.fastpostdelivery.dto.UserDTO;
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

