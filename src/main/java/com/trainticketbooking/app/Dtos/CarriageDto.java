package com.trainticketbooking.app.Dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarriageDto {
    private Integer carriageId;
    private String carNumber;
    private CarriageClassDto carriageClass;
    private int seatCount;
    private int totalFloors;

    public CarriageDto(Integer carriageId, String carNumber) {
        this.carriageId = carriageId;
        this.carNumber = carNumber;
    }
}
