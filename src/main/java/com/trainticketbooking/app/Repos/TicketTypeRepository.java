package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {
    Optional<TicketType> findByName(String name);
}