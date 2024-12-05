package com.trainticketbooking.app.Dtos.Ticket;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketDTO {

    private Integer ticketId;
    private Double price;
    private LocalDateTime bookingDate;
    private String status;
    private Integer seatId;
    private boolean isDeparture;

}