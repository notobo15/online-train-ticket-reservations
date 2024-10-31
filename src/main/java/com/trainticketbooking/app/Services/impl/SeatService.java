package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.SeatDto;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Exceptions.CarriageNotFoundException;
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

    @Autowired
    private CarriageRepository carriageRepository;

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
        if (!this.checkCarriageIfExist(seat.getCarriage().getCarriageId())) {
            throw new CarriageNotFoundException("Carrige does not exist!!");
        }

        Optional<Seat> existingSeatOpt = seatRepository.findById(seat.getSeatId());
        Optional<SeatType> existingSeatTypeOpt = seatTypeRepository.findById(seat.getSeatType().getSeatTypeId());
        if (existingSeatOpt.isPresent()) {
            Seat existingSeat = existingSeatOpt.get();
            existingSeat.setSeatNumber(seat.getSeatNumber());
            existingSeat.setCarriage(seat.getCarriage());
            existingSeat.setFloor(seat.getFloor());
            existingSeat.setSeatType(existingSeatTypeOpt.orElseThrow(
                    () -> new RuntimeException("Seat Type not found with id: " + seat.getSeatType().getSeatTypeId())
            ));
            return seatRepository.save(existingSeat);
        }
        throw new RuntimeException("Seat not found with id: " + seat.getSeatId());
    }

    @Override
    public Page<Seat> findAll(Pageable pageable) {
        return seatRepository.findAll(pageable);
    }

    public List<Seat> findSeatsByCarriageId(Integer carriageId) {
        return seatRepository.findByCarriageCarriageId(carriageId);
    }

    public Boolean checkCarriageIfExist(Integer id) {
        return carriageRepository.existsById(id);
    }


}