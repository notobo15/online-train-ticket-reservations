package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Province.ProvinceDTO;
import com.trainticketbooking.app.Dtos.Station.StationDTO;
import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProvinceMapper {
    ProvinceMapper INSTANCE = Mappers.getMapper(ProvinceMapper.class);

    @Mapping(source = "stations", target = "stations")
    ProvinceDTO toProvinceDTO(Province province);

    @Mapping(source = "stationName", target = "stationName")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "GGMapLink", target = "GGMapLink")
    StationDTO toStationDTO(Station station);
}