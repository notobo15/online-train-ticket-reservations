package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.Carriage.CarriageWithoutSeatsDTO;
import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarriageMapper {
    CarriageMapper INSTANCE = Mappers.getMapper(CarriageMapper.class);

//    @Mapping(source = "carriageClass.name", target = "carriageClassName")
//    @Mapping(target = "seats", expression = "java(toSeatDTOList(carriage))")
//    CarriageDTO toCarriageDTO(Carriage carriage);

//    default List<SeatDTO> toSeatDTOList(Carriage carriage) {
//        return carriage.getCarriageSeatMappings().stream()
//                .map(mapping -> SeatMapper.INSTANCE.to  SeatDTO(mapping.getSeat()))
//                .collect(Collectors.toList());
//    }


    @Mapping(source = "carriageClass.name", target = "carriageClassName")
    @Mapping(target = "seats", expression = "java(toSeatDTOList(carriage.getSeats()))")
    CarriageDTO toCarriageDTO(Carriage carriage);

    default List<SeatDTO> toSeatDTOList(List<Seat> seats) {
        return seats.stream()
                .map(SeatMapper.INSTANCE::toSeatDTO)
                .collect(Collectors.toList());
    }


//    @Mapping(source = "carriageId", target = "carriageId")
//    @Mapping(source = "carriageNumber", target = "carriageNumber")
//    @Mapping(source = "carriageClass.name", target = "carriageClassName")  // Assuming Carriage has a carriageClass
//    @Mapping(source = "carriageClass.carriageClassId", target = "carriageClassId")      // Assuming Carriage has a carriageClassId
//    @Mapping(source = "orderNumber", target = "orderNumber")      // Assuming Carriage has a carriageClassId
//    CarriageWithoutSeatsDTO toCarriageWithoutSeatsDTO(Carriage carriage);
}