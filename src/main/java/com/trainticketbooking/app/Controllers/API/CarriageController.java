package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;
import com.trainticketbooking.app.Services.impl.CarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carriages")
public class CarriageController {

    @Autowired
    private CarriageService carriageService;

    @GetMapping("/by-train/{trainId}")
    public ResponseEntity<List<CarriageDTO>> searchCarriagesByTrain(@PathVariable Integer trainId) {
        List<CarriageDTO> result = carriageService.searchCarriagesByTrain(trainId);
        return ResponseEntity.ok(result);
    }
}
