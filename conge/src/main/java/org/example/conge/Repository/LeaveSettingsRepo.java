package org.example.conge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.conge.entities.LeaveSettings;

import java.util.Optional;

@Repository

public interface LeaveSettingsRepo extends JpaRepository<LeaveSettings, String> {
    Optional<LeaveSettings> findTopByOrderByIdDesc();
}
