package com.speedygo.fastpostdelivery.Repositories;

import com.speedygo.fastpostdelivery.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
