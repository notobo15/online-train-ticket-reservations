package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainService implements ITrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private PriceRepository priceRepository;

    private RouteRepository routeRepository;

//    public List<Seat> findSeatsByTrainId(Integer trainId) {
//        return seatRepository.findSeatsByTrainId(trainId);
//    }

    public List<Carriage> findCarriagesByTrainId(Integer trainId) {
        return carriageRepository.findByTrainTrainId(trainId);
    }

    @Override
    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    @Override
    public Optional<Train> getById(Integer id) {
        return trainRepository.findById(id);
    }

    @Override
    public Train save(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public void deleteById(Integer id) {
        trainRepository.deleteById(id);
    }

    @Override
    public Train update(Train train) {
        Optional<Train> existingTrain = trainRepository.findById(train.getTrainId());
        if (existingTrain.isPresent()) {
            Train updatedTrain = existingTrain.get();
            updatedTrain.setTrainNumber(train.getTrainNumber());
            updatedTrain.setTrainType(train.getTrainType());
            return trainRepository.save(updatedTrain);
        }
        throw new RuntimeException("Train not found with id: " + train.getTrainId());
    }
    
    @Override
    public Page<Train> findAll(Pageable pageable) {
        return trainRepository.findAll(pageable);
    }
}