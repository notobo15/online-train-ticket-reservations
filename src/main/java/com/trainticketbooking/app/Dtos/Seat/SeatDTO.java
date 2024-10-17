package com.trainticketbooking.app.Dtos.Seat;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer seatId;
    private String seatNumber;
    private String seatType;
    private Integer floor;
    private Integer compartmentNumber;
    private boolean status;
}