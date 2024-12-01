package com.trainticketbooking.app.Dtos.TemporaryTicketHold;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TemporaryTicketHoldDTO {
    private Integer holdId;
    private Integer seatId;
    private Integer trainId;
    private Integer departureStationId;
    private Integer arrivalStationId;
    private String departureStationCode;
    private String arrivalStationCode;
    private LocalDate departureDate;
    private LocalDateTime holdStartTime;
    private LocalDateTime expirationTime;
    private Integer userId;
}