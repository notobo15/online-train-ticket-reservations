package com.trainticketbooking.app.Dtos.Route;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RouteDTO {
    private String startStation;
    private String endStation;
    private Double distance;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
}