package com.trainticketbooking.app.Dtos.Seat;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateTemporaryTicketHoldDto {
    private Integer holdId;
    private Integer carriageSeatMappingId;
    private Integer trainId;
    private Integer departureStationId;
    private Integer arrivalStationId;
    private LocalDate departureDate;
}