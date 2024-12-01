package com.trainticketbooking.app.Dtos.Province;

import com.trainticketbooking.app.Dtos.Station.StationDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProvinceDTO {
    private Integer provinceId;
    private String name;
    private String nameEn;
    private String fullName;
    private String fullNameEn;
    private String codeName;
    private List<StationDTO> stations;
}
