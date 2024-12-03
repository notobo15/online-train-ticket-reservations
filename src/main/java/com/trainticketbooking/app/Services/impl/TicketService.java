package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Repos.BookingRepository;
import com.trainticketbooking.app.Repos.CarriageSeatMappingRepository;
import com.trainticketbooking.app.Repos.TicketRepository;
import com.trainticketbooking.app.Services.IBookingService;
import com.trainticketbooking.app.Services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CarriageSeatMappingRepository carriageSeatMappingRepository;


    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> getById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteById(Integer id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket update(Ticket ticket) {
        Ticket existingticket = ticketRepository
                .findById(ticket.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticket.getTicketId()));

        if (Objects.equals(ticket.getStatus(), "Cancelled")) {
            existingticket.setStatus("Cancelled");
            availableCarriageSeatIfAble(ticket);
        }
        else if (Objects.equals(ticket.getStatus(), "Active")){
            checkIfSeatsAreBlocked(ticket);
            existingticket.setStatus("Active");
        }

        return ticketRepository.save(existingticket);
    }

    private void availableCarriageSeatIfAble(Ticket ticket) {
        CarriageSeatMapping csm = carriageSeatMappingRepository.getById(ticket.getCarriageSeatMapping().getCarriageSeatId());

        for (Ticket tit : csm.getTickets()) {
            if (Objects.equals(tit.getStatus(), "Active"))
                return;
        }

        csm.setStatus(false);
        carriageSeatMappingRepository.save(csm);
    }

    private void checkIfSeatsAreBlocked(Ticket ticket) {
        CarriageSeatMapping csm = carriageSeatMappingRepository.getById(ticket.getCarriageSeatMapping().getCarriageSeatId());

        if (csm.isStatus())
            throw new RuntimeException("Seats were taken by another ticket");

        csm.setStatus(true);
        carriageSeatMappingRepository.save(csm);
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }
}
