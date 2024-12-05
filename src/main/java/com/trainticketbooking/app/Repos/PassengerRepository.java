package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByIdentityCardNumber(String identityCardNumber);
}
