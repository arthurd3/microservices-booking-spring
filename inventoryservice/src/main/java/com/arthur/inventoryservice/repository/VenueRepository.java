package com.arthur.inventoryservice.repository;

import com.arthur.inventoryservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
