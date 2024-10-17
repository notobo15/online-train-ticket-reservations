package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SeatMapper {
    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    @Mapping(source = "seatType.seatType", target = "seatType")
    SeatDTO toSeatDTO(Seat seat);
}
