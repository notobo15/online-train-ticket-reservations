package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Carriage.CarriageWithoutSeatsDTO;
import com.trainticketbooking.app.Dtos.Train.TrainWithCarriagesDTO;
import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneyDTO;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.TrainJourney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainJourneyMapper {
    TrainJourneyMapper INSTANCE = Mappers.getMapper(TrainJourneyMapper.class);

    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "arrivalDate", target = "arrivalDate")
    @Mapping(source = "status", target = "status")
    TrainJourneyDTO toTrainJourneyDTO(TrainJourney trainJourney);


}