package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Entities.SeatHold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // Tells MapStruct to generate a Spring Bean
public interface SeatHoldMapper {

    // Mapping the DTO to the Entity (SeatHold)
    @Mapping(source = "seatId", target = "seat.seatId")
    @Mapping(source = "trainId", target = "train.trainId")
    @Mapping(source = "departureStationId", target = "departureStation.stationId")
    @Mapping(source = "arrivalStationId", target = "arrivalStation.stationId")
    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "status", target = "status")
//    @Mapping(source = "isDeparture", target = "isDeparture")
    SeatHold toEntity(CreateSeatHoldRequestDto dto);
}
