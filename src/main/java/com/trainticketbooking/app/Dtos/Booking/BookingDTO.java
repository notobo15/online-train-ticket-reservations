package com.trainticketbooking.app.Dtos.Booking;

import lombok.Data;

import java.util.List;

@Data
public class BookingDTO {
    private Integer userId;
    private Double totalPrice;
    private List<Integer> holdIds;
}