package com.trainticketbooking.app.Dtos.Station;

import lombok.Data;

@Data
public class StationDTO {
    private Integer stationId;
    private String stationName;
    private String code;
    private String GGMapLink;
}
