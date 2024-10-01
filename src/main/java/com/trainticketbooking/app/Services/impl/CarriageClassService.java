package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.CarriageClassRepository;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarriageClassService implements ICarriageClassService {

    @Autowired
    private CarriageClassRepository carriageClassRepository;

    @Override
    public List<CarriageClass> getAll() {
        return carriageClassRepository.findAll();
    }

    @Override
    public Optional<CarriageClass> getById(Integer id) {
        return carriageClassRepository.findById(id);
    }

    @Override
    public CarriageClass save(CarriageClass carriage) {
        return carriageClassRepository.save(carriage);
    }

    @Override
    public void deleteById(Integer id) {
        carriageClassRepository.deleteById(id);
    }

    @Override
    public CarriageClass update(CarriageClass carriage) {
        Optional<CarriageClass> existingCarriage = carriageClassRepository.findById(carriage.getCarriageClassId());
        if (existingCarriage.isPresent()) {
            CarriageClass updatedCarriage = existingCarriage.get();
            updatedCarriage.setName(carriage.getName());
            return carriageClassRepository.save(updatedCarriage);
        } else {
            throw new RuntimeException("Carriage class not found with ID: " + carriage.getCarriageClassId());
        }
    }

}