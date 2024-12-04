package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookingService extends IService<Booking> {
    Page<Booking> findAll(Pageable pageable);
}
