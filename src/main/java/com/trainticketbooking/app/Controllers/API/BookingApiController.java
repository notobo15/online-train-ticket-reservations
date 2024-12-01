package com.trainticketbooking.app.Controllers.API;


import com.trainticketbooking.app.Dtos.Booking.BookingDTO;
import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Services.impl.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingApiController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create-from-holds")
    public Booking createBookingFromHolds(@RequestParam Integer userId, @RequestParam List<Integer> holdIds, @RequestParam Double totalPrice) {
        return bookingService.createBookingFromHolds(userId, holdIds, totalPrice);
    }

    @PostMapping("/confirm-payment/{bookingId}")
    public Booking confirmPayment(@PathVariable Integer bookingId, @RequestParam Double amount) {
        return bookingService.confirmPayment(bookingId, amount);
    }
}