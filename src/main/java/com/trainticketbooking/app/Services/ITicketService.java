package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITicketService extends IService<Ticket> {
    Page<Ticket> findAll(Pageable pageable);
}