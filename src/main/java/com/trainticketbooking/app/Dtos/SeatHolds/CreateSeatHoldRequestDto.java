package com.trainticketbooking.app.Dtos.SeatHolds;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data // Lombok annotation to generate getters, setters, toString(), equals(), and hashCode()
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
public class CreateSeatHoldRequestDto {

    private Integer seatId;
    private Integer trainId;
    private Integer departureStationId;
    private Integer arrivalStationId;
    private LocalDate departureDate;
    private String status;
    private boolean isDeparture;
}