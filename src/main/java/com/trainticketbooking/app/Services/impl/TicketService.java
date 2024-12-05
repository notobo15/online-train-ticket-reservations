package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Repos.TicketRepository;
import com.trainticketbooking.app.Services.ITicketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

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
        // Tìm kiếm đối tượng Ticket bằng ID hoặc ném ngoại lệ nếu không tìm thấy
        Ticket existingTicket = ticketRepository.findById(ticket.getTicketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticket.getTicketId()));

        // Cập nhật các trường nếu chúng không null hoặc hợp lệ
        if (ticket.getPrice() != null && ticket.getPrice() >= 0) {
            existingTicket.setPrice(ticket.getPrice());
        }

        if (ticket.getStatus() != null && !ticket.getStatus().isEmpty()) {
            existingTicket.setStatus(ticket.getStatus());
        }

        if (ticket.getBookingDate() != null) {
            existingTicket.setBookingDate(ticket.getBookingDate());
        }

        if (ticket.getPassenger() != null) {
            existingTicket.setPassenger(ticket.getPassenger());
        }

//        if (ticket.getStartStation() != null) {
//            existingTicket.setStartStation(ticket.getStartStation());
//        }
//
//        if (ticket.getEndStation() != null) {
//            existingTicket.setEndStation(ticket.getEndStation());
//        }
        return ticketRepository.save(existingTicket);
    }


}
