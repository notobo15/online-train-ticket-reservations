package com.trainticketbooking.app.Dtos.Carriage;

import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import lombok.Data;

import java.util.List;

@Data
public class CarriageDTO {
    private Integer carriageId;
    private String carNumber;
    private String carriageClassName;
    private int seatCount;
    private int totalFloors;
    private List<SeatDTO> seats;
}
