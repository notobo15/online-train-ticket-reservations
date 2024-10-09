package com.trainticketbooking.app.mapper;

import com.trainticketbooking.app.Dtos.StationDto;
import com.trainticketbooking.app.Entities.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(source = "provinceCode.code", target = "provinceCode")
    @Mapping(source = "provinceCode.fullName", target = "provinceFullName")
    StationDto toStationDto(Station station);

    List<StationDto> toStationDtoList(List<Station> stations);
}
