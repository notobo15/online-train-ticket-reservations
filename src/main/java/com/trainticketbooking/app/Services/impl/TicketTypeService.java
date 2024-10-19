package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.TicketType;
import com.trainticketbooking.app.Repos.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketTypeService {

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    public Optional<TicketType> findByName(String name) {
        return ticketTypeRepository.findByName(name);
    }
}