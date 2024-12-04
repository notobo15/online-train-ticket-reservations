package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.TicketRepository;
import com.trainticketbooking.app.Services.ITicketService;
import jakarta.persistence.EntityNotFoundException;
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
    private SeatRepository seatRepository;

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

        return ticketRepository.save(existingTicket);
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Override
    public Ticket updateStatus(Ticket ticket) {
        Ticket existingticket = ticketRepository
                .findById(ticket.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticket.getTicketId()));

        if (Objects.equals(ticket.getStatus(), "cancelled")) {
            existingticket.setStatus("cancelled");
            availableCarriageSeatIfAble(ticket);
        }
        else if (Objects.equals(ticket.getStatus(), "active")){
            checkIfSeatsAreBlocked(ticket);
            existingticket.setStatus("active");
        }

        return ticketRepository.save(existingticket);
    }


    private void availableCarriageSeatIfAble(Ticket ticket) {
        Seat seat = seatRepository.getById(ticket.getSeat().getSeatId());

        for (Ticket tit : seat.getTickets()) {
            if (Objects.equals(tit.getStatus(), "active"))
                return;
        }

        seat.setStatus("available");
        seatRepository.save(seat);
    }

    private void checkIfSeatsAreBlocked(Ticket ticket) {
        Seat seat = seatRepository.getById(ticket.getSeat().getSeatId());

        if (Objects.equals(seat.getStatus(), "booked") ||
                Objects.equals(seat.getStatus(), "holding"))
            throw new RuntimeException("Seat was taken or holding by another ticket");

        seat.setStatus("booked");
        seatRepository.save(seat);
    }

}
