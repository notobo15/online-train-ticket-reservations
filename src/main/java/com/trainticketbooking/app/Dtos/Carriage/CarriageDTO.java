package com.trainticketbooking.app.Dtos.Carriage;

import com.trainticketbooking.app.Dtos.Seat.CarriageSeatMappingDTO;
import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import lombok.Data;

import java.util.List;

@Data
public class CarriageDTO {
    private Integer carriageId;
    private String carriageNumber;
    private String carriageClassName;
    private int carriageClassId;
    private int seatCount;
    private int totalFloors;
//    private List<CarriageSeatMappingDTO> seats;
    private List<SeatDTO> seats;
}
