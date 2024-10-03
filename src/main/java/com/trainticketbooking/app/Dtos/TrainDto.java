package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class TrainDto {
    private Long trainId;
    private String trainName;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
}