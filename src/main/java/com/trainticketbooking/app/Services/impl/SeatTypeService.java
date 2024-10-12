package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Repos.SeatTypeRepository;
import com.trainticketbooking.app.Services.ISeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatTypeService implements ISeatTypeService {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Override
    public List<SeatType> getAll() {
        return seatTypeRepository.findAll();
    }

    @Override
    public Optional<SeatType> getById(Integer id) {
        return seatTypeRepository.findById(Long.valueOf(id));
    }

    @Override
    public SeatType save(SeatType seatType) {
        return seatTypeRepository.save(seatType);
    }

    @Override
    public void deleteById(Integer id) {
        seatTypeRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public SeatType update(SeatType seatType) {
        Optional<SeatType> existingSeatType = seatTypeRepository.findById(seatType.getSeatTypeId());
        if (existingSeatType.isPresent()) {
            SeatType updatedSeatType = existingSeatType.get();
            updatedSeatType.setSeatType(seatType.getSeatType());
            updatedSeatType.setCode(seatType.getCode());
            updatedSeatType.setDescription(seatType.getDescription());
            return seatTypeRepository.save(updatedSeatType);
        } else {
            throw new RuntimeException("SeatType not found with ID: " + seatType.getSeatTypeId());
        }
    }
}
