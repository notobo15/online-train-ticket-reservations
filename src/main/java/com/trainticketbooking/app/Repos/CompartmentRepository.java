package com.trainticketbooking.app.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trainticketbooking.app.Entities.Compartment;

@Repository
public interface CompartmentRepository extends JpaRepository<Compartment, Long> {
}