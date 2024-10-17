package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneyDTO;
import com.trainticketbooking.app.Entities.TrainJourney;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainJourneyMapper {
    TrainJourneyMapper INSTANCE = Mappers.getMapper(TrainJourneyMapper.class);

    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "arrivalDate", target = "arrivalDate")
    @Mapping(source = "status", target = "status")
    TrainJourneyDTO toTrainJourneyDTO(TrainJourney trainJourney);
}