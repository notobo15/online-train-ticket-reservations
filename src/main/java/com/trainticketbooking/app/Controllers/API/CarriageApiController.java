package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Services.impl.CarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carriages")
public class CarriageApiController {

    @Autowired
    private CarriageService carriageService;

    @GetMapping("/by-train/{trainId}")
    public ResponseEntity<List<CarriageDTO>> searchCarriagesByTrain(@PathVariable Integer trainId) {
        List<CarriageDTO> result = carriageService.searchCarriagesByTrain(trainId);
        return ResponseEntity.ok(result);
    }

//    @MessageMapping("/getSeatsByCarriage")
//    @SendTo("/topic/seats")
//    @GetMapping("/{carriageId}/seats")
//    public List<CarriageSeatMappingDTO> getSeatsByCarriage(@PathVariable Integer carriageId) {
//        return carriageService.getSeatMappings(carriageId);
//    }

    @GetMapping("/{carriageId}")
    public ResponseEntity<CarriageDTO> getCarriageById(@PathVariable Integer carriageId) {
        // Lấy thông tin của Carriage
        CarriageDTO carriage = carriageService.getCarriageById(carriageId);

        // Lấy số lượng chỗ ngồi của Carriage
        int seatCount = carriageService.getSeatCountByCarriageId(carriageId);
        carriage.setSeatCount(seatCount); // Giả sử CarriageDTO có thuộc tính seatCount

        return ResponseEntity.ok(carriage);
    }
}
