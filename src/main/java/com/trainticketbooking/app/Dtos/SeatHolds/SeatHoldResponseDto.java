package com.trainticketbooking.app.Dtos.SeatHolds;

import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Dtos.Station.StationDTO;
import com.trainticketbooking.app.Dtos.Train.SeatHoldTrainDTO;
import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SeatHoldResponseDto {
    private Integer id;
    private SeatDTO seat;
    private SeatHoldTrainDTO train;
    private StationDTO departureStation;
    private StationDTO arrivalStation;
    private LocalDate departureDate;
    private LocalDateTime holdStartTime;
    private LocalDateTime expirationTime;
    private String status;
    private boolean isDeparture;
}
