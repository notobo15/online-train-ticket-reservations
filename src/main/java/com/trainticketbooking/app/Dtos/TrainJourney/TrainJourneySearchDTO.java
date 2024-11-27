package com.trainticketbooking.app.Dtos.TrainJourney;


import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.SeatType.SeatTypePriceDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class TrainJourneySearchDTO {
    private Integer trainId;
    private String trainNumber;
    private String trainType;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String startProvinceName;
    private String endProvinceName;
    private String startStationName;
    private String endStationName;
    private String startStationCode;
    private String endStationCode;
    private double totalDistance;
    private String totalDuration;
    private List<CarriageDTO> carriages;
    private List<SeatTypePriceDTO> prices;
}