package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.PassengerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerTypeRepository extends JpaRepository<PassengerType, Integer> {
}
