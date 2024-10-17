package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneyDTO;
import com.trainticketbooking.app.Entities.TrainJourney;
import com.trainticketbooking.app.Mappers.TrainMapper;
import com.trainticketbooking.app.Repos.TrainJourneyRepository;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;
import com.trainticketbooking.app.Responses.ApiResponse;
import com.trainticketbooking.app.Services.ITrainJourneyService;
import com.trainticketbooking.app.Services.impl.TrainJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/train-journeys")
public class TrainJourneyController {

    @Autowired
    private TrainJourneyService trainJourneyService;

    @PostMapping("/search")
    public ResponseEntity<List<TrainDTO>> searchTrains(@RequestBody TrainSearchRequestDTO request) {
        List<TrainDTO> result = trainJourneyService.searchTrains(request);
        return ResponseEntity.ok(result);
    }
}