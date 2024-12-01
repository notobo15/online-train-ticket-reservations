package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private String password;
    private String confirmPassword;

}
