package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Entities.Carriage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CarriageMapper {
    CarriageMapper INSTANCE = Mappers.getMapper(CarriageMapper.class);

    @Mapping(source = "carriageClass.name", target = "carriageClassName")
    @Mapping(target = "seats", expression = "java(toSeatDTOList(carriage))")
    CarriageDTO toCarriageDTO(Carriage carriage);

    default List<SeatDTO> toSeatDTOList(Carriage carriage) {
        return carriage.getCarriageSeatMappings().stream()
                .map(mapping -> SeatMapper.INSTANCE.toSeatDTO(mapping.getSeat()))
                .collect(Collectors.toList());
    }
}