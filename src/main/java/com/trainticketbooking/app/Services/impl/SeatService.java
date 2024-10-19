package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.CarriageSeatMappingRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService implements ISeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private CarriageSeatMappingRepository carriageSeatMappingRepository;

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

    public List<CarriageSeatMapping> getSeatsByTrain(Integer trainId) {
        // Tìm tất cả các toa thuộc chuyến tàu được chọn
        List<Carriage> carriages = carriageRepository.findByTrainTrainId(trainId);

        // Duyệt qua tất cả các toa và lấy danh sách ghế từ từng toa
        List<CarriageSeatMapping> seatMappings = new ArrayList<>();
        for (Carriage carriage : carriages) {
            List<CarriageSeatMapping> carriageSeats = carriageSeatMappingRepository.findByCarriageCarriageId(carriage.getCarriageId());
            seatMappings.addAll(carriageSeats);
        }
        return seatMappings;
    }
}