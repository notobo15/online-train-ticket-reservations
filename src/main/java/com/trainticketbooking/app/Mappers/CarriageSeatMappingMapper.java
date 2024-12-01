package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Seat.CarriageSeatMappingDTO;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarriageSeatMappingMapper {
    CarriageSeatMappingMapper INSTANCE = Mappers.getMapper(CarriageSeatMappingMapper.class);
    CarriageSeatMappingDTO toCarriageSeatMappingDTO(CarriageSeatMapping carriageSeatMapping);
}