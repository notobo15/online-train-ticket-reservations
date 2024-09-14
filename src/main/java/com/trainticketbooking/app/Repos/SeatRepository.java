package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Compartment;
import com.trainticketbooking.app.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByCompartment(Compartment compartment);
}