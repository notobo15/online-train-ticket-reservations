package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import com.trainticketbooking.app.Entities.TrainJourney;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;

import java.util.List;

public interface ITrainJourneyService extends IService<TrainJourney>{
    List<TrainDTO> findTrainJourneys(TrainSearchRequestDTO request);
    List<TrainDTO> searchTrains(TrainSearchRequestDTO request);
}
