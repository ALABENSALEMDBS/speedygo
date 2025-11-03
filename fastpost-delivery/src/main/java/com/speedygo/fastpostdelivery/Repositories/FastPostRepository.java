package com.speedygo.fastpostdelivery.Repositories;

import com.speedygo.fastpostdelivery.entities.FastPost;
import com.speedygo.fastpostdelivery.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FastPostRepository extends JpaRepository<FastPost, Long> {
    List<FastPost> findByFastPostStatus(Status status);
}
