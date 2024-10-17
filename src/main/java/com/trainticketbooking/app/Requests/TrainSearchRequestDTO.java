package com.trainticketbooking.app.Requests;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TrainSearchRequestDTO {
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private Integer startStation;
    private Integer endStation;
    private int passengerCount;
    private boolean isRoundTrip;
}