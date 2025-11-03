package org.example.conge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.conge.entities.Leave;

import java.util.List;

@Repository

public interface LeaveRepo extends JpaRepository<Leave, String> {
    List<Leave> findByDriverId(String driverId);

}
