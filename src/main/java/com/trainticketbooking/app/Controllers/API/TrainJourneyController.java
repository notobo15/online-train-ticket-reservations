package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;
import com.trainticketbooking.app.Services.impl.SeatService;
import com.trainticketbooking.app.Services.impl.TrainJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/train-journeys")
public class TrainJourneyController {

    @Autowired
    private TrainJourneyService trainJourneyService;
    @Autowired
    private SeatService seatService;
    @PostMapping("/search")
    public ResponseEntity<List<TrainDTO>> searchTrains(@RequestBody TrainSearchRequestDTO request) {
        List<TrainDTO> result = trainJourneyService.searchTrains(request);
        return ResponseEntity.ok(result);
    }
}