package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Station.StationDTO;
import com.trainticketbooking.app.Entities.Station;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    StationDTO stationToStationDTO(Station station);
}