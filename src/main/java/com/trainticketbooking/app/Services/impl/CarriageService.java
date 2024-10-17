package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Mappers.CarriageMapper;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.TrainRepository;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarriageService implements ICarriageService {

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public List<Carriage> getAll() {
        return carriageRepository.findAll();
    }

    @Override
    public Optional<Carriage> getById(Integer id) {
        return carriageRepository.findById(id);
    }

    @Override
    public Carriage save(Carriage carriage) {
        return carriageRepository.save(carriage);
    }

    @Override
    public void deleteById(Integer id) {
        carriageRepository.deleteById(id);
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


//    public List<Seat> findSeatsByCarriageId(Integer carriageId) {
//        Carriage carriage = carriageRepository.findById(carriageId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid carriage ID: " + carriageId));
//
//        return seatRepository.findByCarriage(carriage);
//    }

    public List<CarriageDTO> searchCarriagesByTrain(Integer trainId) {
        // Tìm Train dựa trên trainId
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        // Lấy danh sách các toa từ Train
        return train.getCarriages().stream()
                .map(CarriageMapper.INSTANCE::toCarriageDTO)
                .collect(Collectors.toList());
    }
}