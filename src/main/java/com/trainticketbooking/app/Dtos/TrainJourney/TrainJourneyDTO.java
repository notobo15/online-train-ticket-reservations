package com.trainticketbooking.app.Dtos.TrainJourney;

import com.trainticketbooking.app.Dtos.TrainDto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class TrainJourneyDTO {
    private Integer trainJourneyId;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String status;
    private List<TrainDto> trains;
}