package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.PassengerType;
import com.trainticketbooking.app.Repos.PassengerTypeRepository;
import com.trainticketbooking.app.Services.IPassengerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerTypeService implements IPassengerTypeService {

    @Autowired
    private PassengerTypeRepository passengerTypeRepository;

    @Override
    public List<PassengerType> getAll() {
        return passengerTypeRepository.findAll();
    }

    @Override
    public Optional<PassengerType> getById(Integer id) {
        return passengerTypeRepository.findById(id);
    }

    @Override
    public PassengerType save(PassengerType passengerType) {
        return passengerTypeRepository.save(passengerType);
    }

    @Override
    public void deleteById(Integer id) {
        passengerTypeRepository.deleteById(id);
    }

    @Override
    public PassengerType update(PassengerType passengerType) {
        Optional<PassengerType> existingPassengerType = passengerTypeRepository.findById(passengerType.getPassengerTypeId());

        if (existingPassengerType.isPresent()) {
            PassengerType updatedPassengerType = existingPassengerType.get();
            updatedPassengerType.setPassengerType(passengerType.getPassengerType());
            updatedPassengerType.setDiscountPercentage(passengerType.getDiscountPercentage());
            return passengerTypeRepository.save(updatedPassengerType);
        } else {
            throw new RuntimeException("PassengerType not found with ID: " + passengerType.getPassengerTypeId());
        }
    }
}
