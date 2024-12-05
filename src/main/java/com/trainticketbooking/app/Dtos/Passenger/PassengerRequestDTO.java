package com.trainticketbooking.app.Dtos.Passenger;


import lombok.Data;
@Data
public class PassengerRequestDTO {

    private String fullName;

    private Integer passengerTypeId;

    private String identityCardNumber;
}

