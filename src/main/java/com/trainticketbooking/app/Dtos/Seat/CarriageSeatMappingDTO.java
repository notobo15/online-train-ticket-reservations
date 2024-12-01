package com.trainticketbooking.app.Dtos.Seat;

import lombok.Data;

@Data
public class CarriageSeatMappingDTO {
    private Integer carriageSeatId;
    private Integer carriageId;
    private String carNumber;
    private Integer seatId;
    private String seatNumber;
    private String seatType;
    private String status; //  "Booked", "Reserved", "Available"
    private String carriageClassName;
}