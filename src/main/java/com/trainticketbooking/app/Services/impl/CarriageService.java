package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.TrainRepository;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarriageService implements ICarriageService {

    @Autowired
    private CarriageRepository carriageRepository;

    @Override
    public List<Carriage> getAll() {
        return carriageRepository.findAll();
    }

    @Override
    public Optional<Carriage> getById(Integer id) {
        return carriageRepository.findById(Long.valueOf(id));
    }

    @Override
    public Carriage save(Carriage carriage) {
        return carriageRepository.save(carriage);
    }

    @Override
    public void deleteById(Integer id) {
        carriageRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public Carriage update(Carriage carriage) {
        Optional<Carriage> existingCarriage = carriageRepository.findById(carriage.getCarriageId());
        if (existingCarriage.isPresent()) {
            Carriage updatedCarriage = existingCarriage.get();
            updatedCarriage.setTrain(carriage.getTrain());
            updatedCarriage.setCarriageClass(carriage.getCarriageClass());
            updatedCarriage.setCarNumber(carriage.getCarNumber());
            updatedCarriage.setSeatCount(carriage.getSeatCount());
            updatedCarriage.setTotalFloors(carriage.getTotalFloors());
            return carriageRepository.save(updatedCarriage);
        } else {
            throw new RuntimeException("Carriage not found with ID: " + carriage.getCarriageId());
        }
    }
}