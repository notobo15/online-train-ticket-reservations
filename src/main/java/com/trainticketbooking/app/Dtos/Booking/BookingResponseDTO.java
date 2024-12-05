package com.trainticketbooking.app.Dtos.Booking;

import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Dtos.Ticket.TicketResponseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDTO {

    private Integer bookingId;
    private LocalDateTime bookingTime;
    private Double totalPrice;
    private String startStation;
    private String endStation;
    private LocalDate departureDate;
    private List<TicketResponseDTO> tickets;  // List of tickets associated with the booking

}