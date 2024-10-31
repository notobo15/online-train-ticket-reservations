package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Repos.SeatTypeRepository;
import com.trainticketbooking.app.Services.ISeatTypeService;
import org.springframework.beans.BeanUtils;
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
        Optional<SeatType> existingSeatTypeOpt = seatTypeRepository.findById(seatType.getSeatTypeId());

        if (existingSeatTypeOpt.isPresent()) {
            SeatType existingSeatType = existingSeatTypeOpt.get();
            existingSeatType.setCode(seatType.getCode());
            existingSeatType.setDescription(seatType.getDescription());
            existingSeatType.setSeatType(seatType.getSeatType());

            return seatTypeRepository.save(existingSeatType);
        }
        throw new RuntimeException("Seat type not found with id: " + seatType.getSeatTypeId());
    }
}
