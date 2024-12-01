package com.trainticketbooking.app.Dtos.Seat;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TemporaryTicketHoldDto extends CreateTemporaryTicketHoldDto {

    private LocalDateTime holdStartTime;
    private LocalDateTime expirationTime;
}