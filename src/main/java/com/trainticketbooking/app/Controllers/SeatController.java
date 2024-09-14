package com.trainticketbooking.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SeatController {

//    @Autowired
//    private SeatService seatService;

    // Lấy danh sách ghế theo mã tàu
//    @GetMapping("/seats")
//    public List<SeatDto> getSeats(@RequestParam Long trainId) {
//        return seatService.getSeats(trainId);
//    }
}