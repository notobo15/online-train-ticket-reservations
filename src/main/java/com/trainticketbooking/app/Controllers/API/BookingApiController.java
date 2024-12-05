package com.trainticketbooking.app.Controllers.API;


import com.trainticketbooking.app.Dtos.Booking.BookingDTO;
import com.trainticketbooking.app.Dtos.Booking.BookingRequestDTO;
import com.trainticketbooking.app.Dtos.Booking.BookingResponseDTO;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Dtos.Wrappers.ApiResponse;
import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Services.impl.BookingService;
import com.trainticketbooking.app.Services.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingApiController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create-from-holds")
    public Booking createBookingFromHolds(@RequestParam Integer userId, @RequestParam List<Integer> holdIds, @RequestParam Double totalPrice) {
        return bookingService.createBookingFromHolds(userId, holdIds, totalPrice);
    }

    @PostMapping("/confirm-payment/{bookingId}")
    public Booking confirmPayment(@PathVariable Integer bookingId, @RequestParam Double amount) {
        return bookingService.confirmPayment(bookingId, amount);
    }

    @PostMapping
    public ApiResponse<SeatHoldResponseDto> createBooking(@RequestBody BookingRequestDTO bookingDTO) {
        try {
            // Create the booking
            bookingService.createBooking(bookingDTO);
            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(null)
                    .message("Create successfully.")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(null)
                    .success(false)
                    .message("Booking khong thanh cong")
                    .build();
        }
    }

    // Get booking details by ID
    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponseDTO> getBookingById(@PathVariable Integer bookingId) {
        BookingResponseDTO bookingDTO = bookingService.getBookingById(bookingId);
        if (bookingDTO != null) {
            return ApiResponse.<BookingResponseDTO>builder()
                    .result(bookingDTO)
                    .message("get booking by id")
                    .build();
        } else {
            return ApiResponse.<BookingResponseDTO>builder()
                    .result(null)
                    .message("Booking khong thanh cong")
                    .build();
        }
    }

    // Get a list of all bookings
    @GetMapping
    public  ApiResponse<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ApiResponse.<List<BookingResponseDTO>>builder()
                .result(bookings)
                .build();
    }
}