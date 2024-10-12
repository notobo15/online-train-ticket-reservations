package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Repos.CarriageSeatMappingRepository;
import com.trainticketbooking.app.Services.ICarriageSeatMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarriageSeatMappingService implements ICarriageSeatMappingService {

    @Autowired
    private CarriageSeatMappingRepository carriageSeatMappingRepository;

    @Override
    public List<CarriageSeatMapping> getAll() {
        return carriageSeatMappingRepository.findAll();
    }

    @Override
    public Optional<CarriageSeatMapping> getById(Integer id) {
        return carriageSeatMappingRepository.findById(id);
    }

    @Override
    public CarriageSeatMapping save(CarriageSeatMapping carriageSeatMapping) {
        return carriageSeatMappingRepository.save(carriageSeatMapping);
    }

    @Override
    public void deleteById(Integer id) {
        carriageSeatMappingRepository.deleteById(id);
    }

    @Override
    public CarriageSeatMapping update(CarriageSeatMapping carriageSeatMapping) {
        Optional<CarriageSeatMapping> existingMapping = carriageSeatMappingRepository.findById(carriageSeatMapping.getCarriageSeatId());
        if (existingMapping.isPresent()) {
            CarriageSeatMapping updatedMapping = existingMapping.get();
            updatedMapping.setCarriage(carriageSeatMapping.getCarriage());
            updatedMapping.setSeat(carriageSeatMapping.getSeat());
            updatedMapping.setStatus(carriageSeatMapping.isStatus());
            return carriageSeatMappingRepository.save(updatedMapping);
        } else {
            throw new RuntimeException("CarriageSeatMapping not found with ID: " + carriageSeatMapping.getCarriageSeatId());
        }
    }
}