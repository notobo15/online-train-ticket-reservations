package com.trainticketbooking.app.Dtos.Passenger;

import lombok.Data;

@Data
public class PassengerResponseDTO {

    private Integer PassengerId;
    private String fullName;
    private String PassengerType;
    private String identityCardNumber;
}
