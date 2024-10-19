package com.trainticketbooking.app.Requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketRequestDTO {
    private Integer startStationId;
    private Integer endStationId;
    private LocalDate departureDate;
    private Integer seatId;
    private Double price;
    private String ticketTypeName;
}