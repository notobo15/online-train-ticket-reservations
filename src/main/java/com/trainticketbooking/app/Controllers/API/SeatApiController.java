package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Services.impl.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seats")
public class SeatApiController {

    @Autowired
    private SeatService seatService;

    @PutMapping("/{seatId}/status")
    public ResponseEntity<String> updateSeatStatus(
            @PathVariable Integer seatId,
            @RequestParam String status) {

        seatService.updateSeatStatus(seatId, status);
        return ResponseEntity.ok("Seat status updated successfully");
    }
}