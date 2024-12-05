package com.trainticketbooking.app.Dtos.Ticket;

import com.trainticketbooking.app.Dtos.Passenger.PassengerResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponseDTO {

    private Integer ticketId;
    private Double price;
    private LocalDateTime bookingDate;
    private String status;
    private Integer seatId;
    private String seatNumber;
    private String seatType;

    private boolean isDeparture;


    private PassengerResponseDTO passenger;

}