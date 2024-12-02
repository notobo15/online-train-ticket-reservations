package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(value = """
            SELECT t.*
            FROM tickets t
            JOIN carriage_seat_mapping csm ON t.carriage_seat_id = csm.carriage_seat_id
            WHERE csm.carriage_id = :carriageId
            """, nativeQuery = true)
    List<Ticket> getTicketsByCarriageId(@Param("carriageId") Integer carriageId);
}
