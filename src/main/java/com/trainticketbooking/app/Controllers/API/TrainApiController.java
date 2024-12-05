package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Train.TrainWithCarriagesDTO;
import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneySearchDTO;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;
import com.trainticketbooking.app.Services.impl.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainApiController {
    @Autowired
    private TrainService trainService;

    @PostMapping("/search")
    public ResponseEntity<List<TrainJourneySearchDTO>> searchTrains(@RequestBody TrainSearchRequestDTO request) {
        List<TrainJourneySearchDTO> results = trainService.searchTrainJourneys(request);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/{trainId}/carriages")
    public TrainWithCarriagesDTO getTrainWithCarriages(@PathVariable Integer trainId) {
        return trainService.findByTrainTrainId(trainId);
    }
}