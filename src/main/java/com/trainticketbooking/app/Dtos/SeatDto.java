package com.trainticketbooking.app.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

    private Integer seatId;
    private String seatNumber;
    private boolean status;

    public SeatDto(String seatNumber, boolean status) {
        this.seatNumber = seatNumber;
        this.status = status;
    }
}