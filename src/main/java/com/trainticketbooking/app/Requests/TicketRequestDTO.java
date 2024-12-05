package com.trainticketbooking.app.Requests;

import com.trainticketbooking.app.Dtos.Passenger.PassengerRequestDTO;
import com.trainticketbooking.app.Entities.Passenger;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketRequestDTO {
    private Integer startStationId;
    private Integer endStationId;
    private LocalDate departureDate;
    private Integer seatId;
    private Double price;
    private boolean isDeparture;
    private PassengerRequestDTO passenger;
}