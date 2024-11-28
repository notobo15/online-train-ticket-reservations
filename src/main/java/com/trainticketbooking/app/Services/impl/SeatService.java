package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.SeatDto;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Train;

import com.trainticketbooking.app.Exceptions.SeatTypeNotFoundException;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.SeatTypeRepository;
import com.trainticketbooking.app.Services.ISeatService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService implements ISeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;

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
        Optional<SeatType> existingSeatTypeOpt = seatTypeRepository.findById(seat.getSeatType().getSeatTypeId());
        seat.setSeatType(existingSeatTypeOpt.orElseThrow(
                () -> new SeatTypeNotFoundException("Seat Type not found with id: " + seat.getSeatType().getSeatTypeId())
        ));
        return seatRepository.save(seat);
    }

    @Override
    public void deleteById(Integer id) {
        seatRepository.deleteById(id);
    }

    @Override
    public Seat update(Seat seat) {
        Optional<Seat> existingSeatOpt = seatRepository.findById(seat.getSeatId());
        Optional<SeatType> existingSeatTypeOpt = seatTypeRepository.findById(seat.getSeatType().getSeatTypeId());

        Seat existingSeat = existingSeatOpt.orElseThrow(
                () -> new RuntimeException("Seat not found with id: " + seat.getSeatId())
        );
        existingSeat.setSeatNumber(seat.getSeatNumber());
        existingSeat.setFloor(seat.getFloor());
        existingSeat.setCompartmentNumber(seat.getCompartmentNumber());
        existingSeat.setStatus(seat.getStatus());
        existingSeat.setSeatType(existingSeatTypeOpt.orElseThrow(
                () -> new SeatTypeNotFoundException("Seat Type not found with id: " + seat.getSeatType().getSeatTypeId())
        ));
        return seatRepository.save(existingSeat);

    }

    @Override
    public Page<Seat> findAll(Pageable pageable) {
        return seatRepository.findAll(pageable);
    }

}