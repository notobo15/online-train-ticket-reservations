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
    private Integer seatId;  // ID của ga đến
    private String status;  // ID của ga đến

    private LocalDate departureDate;  // Ngày khởi hành

    private Integer carriageId;

    public Integer getArrivalStationId() {
        return arrivalStationId;
    }

    public void setArrivalStationId(Integer arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public Integer getDepartureStationId() {
        return departureStationId;
    }

    public void setDepartureStationId(Integer departureStationId) {
        this.departureStationId = departureStationId;
    }

    public String getDepartureStationCode() {
        return departureStationCode;
    }

    public void setDepartureStationCode(String departureStationCode) {
        this.departureStationCode = departureStationCode;
    }

    public String getArrivalStationCode() {
        return arrivalStationCode;
    }

    public void setArrivalStationCode(String arrivalStationCode) {
        this.arrivalStationCode = arrivalStationCode;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getCarriageId() {
        return carriageId;
    }

    public void setCarriageId(Integer carriageId) {
        this.carriageId = carriageId;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
