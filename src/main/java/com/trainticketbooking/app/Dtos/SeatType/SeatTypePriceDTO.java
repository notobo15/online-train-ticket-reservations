package com.trainticketbooking.app.Dtos.SeatType;

import lombok.Data;

@Data
public class SeatTypePriceDTO {
    private Long seatTypeId;
    private String seatType;
    private String code;
    private String description;
    private Double totalPrice;
}