package com.trainticketbooking.app.Dtos.Ticket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketRequestDTO {
    private Double price;
    private LocalDateTime bookingDate;
    private Integer seatId;
    private boolean isDeparture;
}