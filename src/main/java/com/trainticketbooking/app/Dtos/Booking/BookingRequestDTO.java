package com.trainticketbooking.app.Dtos.Booking;

import com.trainticketbooking.app.Dtos.Ticket.TicketResponseDTO;
import com.trainticketbooking.app.Requests.TicketRequestDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingRequestDTO {

    private LocalDateTime bookingTime;
    private Integer startStationId;
    private Integer endStationId;
    private List<TicketRequestDTO> tickets;

}