package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Services.IBookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/bookings")
public class BookingAdminController {

    @Autowired
    private IBookingService mBookingService;


    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Booking> bookingPage = mBookingService.findAll(pageRequest);

        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingPage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/bookings/index?page=%d&size=%d", page, size);
        session.setAttribute("previousBookingsPage", currentUrl);

        return "admin/bookings/index";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Booking> bookingOpt = mBookingService.getById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            model.addAttribute("booking", booking);
            model.addAttribute("ticketsOfBooking",booking.getTickets());
            return "admin/bookings/detail";
        }
        return (String) session.getAttribute("previousBookingsPage");
    }

}
