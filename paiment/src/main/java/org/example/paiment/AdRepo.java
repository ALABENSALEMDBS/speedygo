package org.example.paiment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
    // Simple CRUD repository – no extra methods needed
    //
}
