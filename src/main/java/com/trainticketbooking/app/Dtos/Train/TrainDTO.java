package com.trainticketbooking.app.Dtos.Train;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.TrainJourney;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class TrainDTO {
    private Integer trainId;
    private String trainType;
    private String trainNumber;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String totalDuration;
    private double totalDistance;
}
