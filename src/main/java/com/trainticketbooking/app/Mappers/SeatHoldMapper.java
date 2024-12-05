package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Dtos.Train.SeatHoldTrainDTO;
import com.trainticketbooking.app.Entities.SeatHold;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Train;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")  // Tells MapStruct to generate a Spring Bean
public interface SeatHoldMapper {

    // Mapping the DTO to the Entity (SeatHold)
    @Mapping(source = "seatId", target = "seat.seatId")
    @Mapping(source = "trainId", target = "train.trainId")
    @Mapping(source = "departureStationId", target = "departureStation.stationId")
    @Mapping(source = "arrivalStationId", target = "arrivalStation.stationId")
    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "status", target = "status")
    SeatHold toEntity(CreateSeatHoldRequestDto dto);

    // Mapping SeatHold entity to SeatHoldResponseDto
    @Mappings({
            @Mapping(source = "seat", target = "seat"),
            @Mapping(source = "train", target = "train"),
            @Mapping(source = "departureStation", target = "departureStation"),
            @Mapping(source = "arrivalStation", target = "arrivalStation")
    })
    SeatHoldResponseDto toDto(SeatHold seatHold);

    // Custom method to map SeatType to String (seatType)
    default String mapSeatTypeToString(SeatType seatType) {
        return seatType != null ? seatType.getSeatType() : null;
    }

    // Custom method to map Seat to SeatDTO
    default SeatDTO seatToSeatDTO(Seat seat) {
        if (seat == null) {
            return null;
        }

        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setSeatId(seat.getSeatId());
        seatDTO.setSeatNumber(seat.getSeatNumber());
        seatDTO.setSeatType(mapSeatTypeToString(seat.getSeatType()));
        seatDTO.setStatus(seat.getStatus());

        return seatDTO;
    }

}
