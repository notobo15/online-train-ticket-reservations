package com.trainticketbooking.app.Dtos.Booking;

import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {

    private Integer bookingId;
    private LocalDateTime bookingTime;
    private Double totalPrice;
    private Integer startStationId;
    private Integer endStationId;
    private LocalDate departureDate;
    private List<TicketDTO> tickets;  // List of tickets associated with the booking

}