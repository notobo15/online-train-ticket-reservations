package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Seat.TemporaryTicketHoldDto;
import com.trainticketbooking.app.Dtos.TemporaryTicketHold.TemporaryTicketHoldDTO;
import com.trainticketbooking.app.Entities.TemporaryTicketHold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemporaryTicketHoldMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "seat.seatId", target = "seatId")
    @Mapping(source = "departureStation.code", target = "departureStationCode")
    @Mapping(source = "arrivalStation.code", target = "arrivalStationCode")
    @Mapping(source = "departureStation.stationId", target = "departureStationId")
    @Mapping(source = "arrivalStation.stationId", target = "arrivalStationId")
    TemporaryTicketHoldDTO toDTO(TemporaryTicketHold entity);

    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "seatId", target = "seat.seatId")
    @Mapping(source = "departureStationCode", target = "departureStation.code")
    @Mapping(source = "arrivalStationCode", target = "arrivalStation.code")
    @Mapping(source = "departureStationId", target = "departureStation.stationId")
    @Mapping(source = "arrivalStationId", target = "arrivalStation.stationId")
    TemporaryTicketHold toEntity(TemporaryTicketHoldDTO dto);
}