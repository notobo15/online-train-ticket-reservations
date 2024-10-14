package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class OrderRequestDto {
    private int amount;
    private String orderInfo;
}
