package com.trainticketbooking.app.Dtos.Train;

import lombok.Data;

import java.time.LocalTime;


@Data
public class SeatHoldTrainDTO {
    private Integer trainId;
    private String trainType;
    private String trainNumber;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String totalDuration;
    private double totalDistance;
}
