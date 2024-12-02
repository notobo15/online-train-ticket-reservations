package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarriageSeatMappingRepository extends JpaRepository<CarriageSeatMapping, Integer> {

    List<CarriageSeatMapping> findByCarriage_CarriageId(Integer carriageId);

    @Query(value = """
            SELECT COUNT(t.ticket_id)
            FROM tickets t
            JOIN carriage_seat_mapping csm ON t.carriage_seat_id = csm.carriage_seat_id
            WHERE csm.carriage_id = :carriageId
            """, nativeQuery = true)
    Integer countTicketsByCarriageId(@Param("carriageId") Integer carriageId);

    Optional<CarriageSeatMapping> findByCarriage_CarriageIdAndSeat_SeatId(Integer carriageId, Integer seatId);
}
