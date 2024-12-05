package com.trainticketbooking.app.Dtos.SeatHolds;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SeatHoldRequestDto {
    private Integer trainId;  // ID của tàu
    private Integer departureStationId;  // ID của ga đi
    private Integer arrivalStationId;  // ID của ga đến
    private String departureStationCode;  // ID của ga đi
    private String arrivalStationCode;  // ID của ga đến

    private LocalDate departureDate;  // Ngày khởi hành

    private Integer carriageId;
}
