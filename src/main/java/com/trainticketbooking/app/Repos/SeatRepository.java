package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
//    List<Seat> findByCarriageCarriageId(Integer carriageId);

//    List<Seat> findBySeatTypeSeatTypeId(Integer seatTypeId);

//    @Query("SELECT s FROM Seat s WHERE s.carriage.train.trainId = :trainId")
//    List<Seat> findSeatsByTrainId(@Param("trainId") Integer trainId);

//    List<Seat> findByCarriage(Carriage carriage);
}