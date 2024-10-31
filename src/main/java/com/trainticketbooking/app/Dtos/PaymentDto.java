package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class PaymentDto {
    private String orderId;
    private String totalPrice;
    private String paymentTime;
    private String transactionId;
    private String bankCode;
    private String responseCode;
}

