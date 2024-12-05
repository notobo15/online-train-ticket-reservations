package com.trainticketbooking.app.Dtos.Seat;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer seatId;
    private String seatNumber;
    private String seatType;
    private String status; // "Booked," "Reserved," "Available"
}