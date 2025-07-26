package com.arthur.inventoryservice.repository;

import com.arthur.inventoryservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
