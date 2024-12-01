package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.TemporaryTicketHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemporaryTicketHoldRepository extends JpaRepository<TemporaryTicketHold, Integer> {

//    List<TemporaryTicketHold> findByUserId(Integer userId);

    List<TemporaryTicketHold> findByExpirationTimeBefore(LocalDateTime now);

    void deleteByExpirationTimeBefore(LocalDateTime now);

    List<TemporaryTicketHold> findBySeat_SeatIdAndTrainIdAndDepartureDate(
            Integer seatId,
            Integer trainId,
            LocalDate departureDate
    );
}
