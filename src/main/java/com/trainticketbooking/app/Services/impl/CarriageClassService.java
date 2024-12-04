package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.CarriageClassRepository;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public CarriageClass save(CarriageClass cac) {
        return carriageClassRepository.save(cac);
    }

    @Override
    public void deleteById(Integer id) {
        carriageClassRepository.deleteById(id);
    }

    @Override
    public CarriageClass update(CarriageClass cac) {
        CarriageClass existingCarriageClass = carriageClassRepository
                .findById(cac.getCarriageClassId())
                .orElseThrow(() -> new RuntimeException("Carriage class not found with ID: " + cac.getCarriageClassId()));

        existingCarriageClass.setName(cac.getName());

        return carriageClassRepository.save(existingCarriageClass);
    }

    @Override
    public Page<CarriageClass> findAll(Pageable pageable) {
        return carriageClassRepository.findAll(pageable);
    }

}