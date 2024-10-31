package com.trainticketbooking.app.Models;

import com.trainticketbooking.app.Validations.PasswordMatches;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@PasswordMatches
public class ResetPassword {

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;

    private String token;
}
