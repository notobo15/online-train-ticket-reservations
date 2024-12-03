package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Exceptions.CarriageClassNotFoundException;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Services.ICarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarriageService implements ICarriageService {

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CarriageSeatMappingRepository carriageSeatMappingRepository;

    @Autowired
    private CarriageClassRepository carriageClassRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TicketRepository ticketRepository;

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
        Carriage existingCarriage = carriageRepository
                .findById(carriage.getCarriageId())
                .orElseThrow(
                        () -> new RuntimeException("Carriage not found with ID: " + carriage.getCarriageId()));
        CarriageClass existingCarriageClass = carriageClassRepository
                .findById(carriage.getCarriageClass().getCarriageClassId())
                .orElseThrow(
                        () -> new CarriageClassNotFoundException("Carriage Class not found with ID: " + carriage.getCarriageClass().getCarriageClassId()));

        Train existingCTrain = trainRepository
                .findById(carriage.getTrain().getTrainId())
                .orElseThrow(
                        () -> new RuntimeException("Train not found with ID: " + carriage.getTrain().getTrainId()));


        existingCarriage.setTrain(existingCTrain);
        existingCarriage.setCarriageClass(existingCarriageClass);
        existingCarriage.setCarNumber(carriage.getCarNumber());
        existingCarriage.setSeatCount(carriage.getSeatCount());
        existingCarriage.setTotalFloors(carriage.getTotalFloors());

        return carriageRepository.save(existingCarriage);
    }

    @Override
    public Page<Carriage> findAll(Pageable pageable) {
        return carriageRepository.findAll(pageable);
    }

    public List<CarriageSeatMapping> getCSMsByCarriageId(Integer carriageId) {
        return carriageSeatMappingRepository.findByCarriage_CarriageId(carriageId);
    }

    public Integer getTicketNumberOfCarriage(Integer carriageId) {
        List<CarriageSeatMapping> csmMappings = carriageSeatMappingRepository.findByCarriage_CarriageId(carriageId);
        int sum = 0;

        for (CarriageSeatMapping csm : csmMappings) {
            sum += csm.getTickets().size();
        }
        return sum;
    }

    public List<Ticket> getTicketsOfCarriage(Integer carriageId) {
        return ticketRepository.getTicketsByCarriageId(carriageId);
    }


    public List<Seat> getSeatsOfCarriage(List<CarriageSeatMapping> csmMappings) {
//        List<CarriageSeatMapping> csmMappings = carriageSeatMappingRepository.findByCarriage_CarriageId(carriageId);
        List<Seat> seats = new ArrayList<>() ;

        for (CarriageSeatMapping csm : csmMappings) {
            seats.add(csm.getSeat());
        }

        return seats;
    }

    @Override
    public CarriageSeatMapping getCSMByCarriageIdAndSeatId(Integer carriageId, Integer seatId) {
        Optional<CarriageSeatMapping> csmOpt = carriageSeatMappingRepository.
                findByCarriage_CarriageIdAndSeat_SeatId(carriageId,seatId);

        return csmOpt.orElseThrow(
                () -> new RuntimeException("CarriageSeatMapping not found with carriage ID: "
                        +carriageId +
                        " and seat ID: " + seatId)
        );
    }
}