package com.trainticketbooking.app.Dtos.Ticket;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long ticketId;
    private String status;
    private LocalDateTime bookingDate;
    private Double price;
    private LocalDate departureDate;
    private Long startStationId;
    private Long endStationId;
    private Long seatId;
}