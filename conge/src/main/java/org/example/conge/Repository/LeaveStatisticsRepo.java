package org.example.conge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.conge.entities.LeaveStatistics;

@Repository
public interface LeaveStatisticsRepo extends JpaRepository<LeaveStatistics, String> {

}
