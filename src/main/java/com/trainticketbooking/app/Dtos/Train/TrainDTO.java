package com.trainticketbooking.app.Dtos.Train;

import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneyDTO;
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
    private List<TrainJourneyDTO> journeys;
}
