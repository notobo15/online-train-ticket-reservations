package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Booking;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
//    Booking findByOrderInfo(String orderInfo);
    Booking findByBookingId(Integer orderId);
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.tickets WHERE b.bookingId = :bookingId")
    Optional<Booking> findByBookingIdWithTickets(@Param("bookingId") Integer bookingId);
}