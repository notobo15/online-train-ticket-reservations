package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Entities.SeatHold;
import com.trainticketbooking.app.Services.impl.SeatHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seatholds")
public class SeatHoldController {

    private final SeatHoldService seatHoldService;

    @Autowired
    public SeatHoldController(SeatHoldService seatHoldService) {
        this.seatHoldService = seatHoldService;
    }

    // Endpoint to create a seat hold
    @PostMapping
    public ResponseEntity<String> createSeatHold(@RequestBody CreateSeatHoldRequestDto seatHoldRequestDto) {
        seatHoldService.createSeatHold(seatHoldRequestDto);
        return ResponseEntity.ok("SeatHold created successfully.");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeatHold(@PathVariable Integer id) {
        try {
            seatHoldService.deleteSeatHoldById(id);
            return ResponseEntity.ok("SeatHold with ID " + id + " has been deleted.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(ex.getMessage()); // Return not found if exception occurs
        }
    }
    // Other endpoints would go here
}