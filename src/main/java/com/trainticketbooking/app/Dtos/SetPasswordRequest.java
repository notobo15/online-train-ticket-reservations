package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String email;
    private String password;
}