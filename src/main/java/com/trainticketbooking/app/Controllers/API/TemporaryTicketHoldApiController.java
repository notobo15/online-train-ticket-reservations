package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Seat.CreateTemporaryTicketHoldDto;
import com.trainticketbooking.app.Dtos.Seat.TemporaryTicketHoldDto;
import com.trainticketbooking.app.Dtos.TemporaryTicketHold.TemporaryTicketHoldDTO;
import com.trainticketbooking.app.Entities.TemporaryTicketHold;
import com.trainticketbooking.app.Mappers.TemporaryTicketHoldMapper;
import com.trainticketbooking.app.Services.impl.TemporaryTicketHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ticket-holds")
public class TemporaryTicketHoldApiController {

    @Autowired
    private TemporaryTicketHoldService temporaryTicketHoldService;

//    @PostMapping("/hold")
//    public ResponseEntity<TemporaryTicketHoldDto> holdTicket(@RequestBody CreateTemporaryTicketHoldDto ticketHoldDto) {
//        TemporaryTicketHoldDto savedHold = temporaryTicketHoldService.holdTicket(ticketHoldDto);
//        return ResponseEntity.ok(savedHold);
//    }
//
//    @GetMapping("/{holdId}")
//    public ResponseEntity<TemporaryTicketHoldDto> getHoldById(@PathVariable Integer holdId) {
//        Optional<TemporaryTicketHoldDto> ticketHold = temporaryTicketHoldService.findHoldById(holdId);
//        return ticketHold.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/release/{holdId}")
//    public ResponseEntity<Void> releaseHold(@PathVariable Integer holdId) {
//        temporaryTicketHoldService.releaseHold(holdId);
//        return ResponseEntity.noContent().build();
//    }
@GetMapping
public List<TemporaryTicketHoldDTO> getAllHolds() {
    return temporaryTicketHoldService.findAll();
}

    @GetMapping("/{id}")
    public Optional<TemporaryTicketHoldDTO> getHoldById(@PathVariable Integer id) {
        return temporaryTicketHoldService.findById(id);
    }

    @PostMapping
    public TemporaryTicketHoldDTO createHold(@RequestBody TemporaryTicketHoldDTO dto) {
        return temporaryTicketHoldService.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteHold(@PathVariable Integer id) {
        temporaryTicketHoldService.deleteById(id);
    }
}

