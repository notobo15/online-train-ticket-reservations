package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
//    Booking findByOrderInfo(String orderInfo);
//    Booking findByOrderId(Integer orderId);
}