package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class OrderResponseDto {
    private String message;
    private String url;
    private boolean status;
}
