package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface ITicketService  extends IService<Ticket>{
    Page<Ticket> findAll(Pageable pageable);

    public Ticket updateStatus(Ticket t);
}
