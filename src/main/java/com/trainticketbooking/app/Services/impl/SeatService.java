package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.SeatDto;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService implements ISeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    @Override
    public Optional<Seat> getById(Integer id) {
        return seatRepository.findById(id);
    }

    @Override
    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    public void deleteById(Integer id) {
        seatRepository.deleteById(id);
    }

    @Override
    public Seat update(Seat seat) {
        Optional<Seat> existingSeat = seatRepository.findById(seat.getSeatId());
        if (existingSeat.isPresent()) {
            Seat updatedSeat = existingSeat.get();
            updatedSeat.setSeatNumber(seat.getSeatNumber());
            updatedSeat.setFloor(seat.getFloor());
            return seatRepository.save(updatedSeat);
        }
        throw new RuntimeException("Seat not found with id: " + seat.getSeatId());
    }

//    public List<Seat> findSeatsByCarriageId(Integer carriageId) {
//        return seatRepository.findByCarriageCarriageId(carriageId);
//    }
}