package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatHold;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHold, Integer> {

    // Find a SeatHold by its ID
    Optional<SeatHold> findById(Integer id);

    // Find all SeatHolds by trainId
    List<SeatHold> findByTrainTrainId(Integer trainId);

    // Find all SeatHolds by departure station
    List<SeatHold> findByDepartureStationStationId(Integer departureStationStationId);

    // Find all SeatHolds by status
    List<SeatHold> findByStatus(String status);

    // Custom query example: Find SeatHolds that are about to expire
    List<SeatHold> findByExpirationTimeBeforeAndStatus(LocalDateTime time, String status);

    List<SeatHold> findByTrainAndDepartureDate(Train train, LocalDate departureDate);
    Optional<SeatHold> findBySeat_SeatIdAndDepartureDate(Integer seatId, LocalDate departureDate);
    List<SeatHold> findByTrainAndDepartureDateAndSeat(Train train, LocalDate departureDate, Seat seat);
}