package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Passenger;
import com.trainticketbooking.app.Repos.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    // Find passenger by ID
    public Optional<Passenger> getPassengerById(Integer passengerId) {
        return passengerRepository.findById(passengerId);
    }

    // Find passenger by Identity Card Number
    public Optional<Passenger> getPassengerByIdentityCardNumber(String identityCardNumber) {
        return passengerRepository.findByIdentityCardNumber(identityCardNumber);
    }

    // Save a new passenger
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    // Delete a passenger by ID
    public void deletePassenger(Integer passengerId) {
        passengerRepository.deleteById(passengerId);
    }

    // Update a passenger's details
    public Passenger updatePassenger(Passenger passenger) {
        if (passengerRepository.existsById(passenger.getPassengerId())) {
            return passengerRepository.save(passenger);
        }
        return null;
    }
}
