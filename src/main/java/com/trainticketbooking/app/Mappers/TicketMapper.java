package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "startStation.stationId", target = "startStationId")
    @Mapping(source = "endStation.stationId", target = "endStationId")
    TicketDTO toDTO(Ticket ticket);
}
