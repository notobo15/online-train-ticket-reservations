package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Exceptions.SeatTypeNotFoundException;
import com.trainticketbooking.app.Services.ICarriageSeatMappingService;
import com.trainticketbooking.app.Services.ITicketService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/tickets")
public class TicketAdminController {

    @Autowired
    private ITicketService mTicketService;

    @Autowired
    private ICarriageSeatMappingService mCarriageSeatMappingService;

    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Ticket> ticketPage = mTicketService.findAll(pageRequest);

        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/tickets/index?page=%d&size=%d", page, size);
        session.setAttribute("previousTicketsPage", currentUrl);

        return "admin/tickets/index";
    }


    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Ticket> ticketOpt = mTicketService.getById(id);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            model.addAttribute("ticket", ticket);
            return "admin/tickets/edit";
        }
        return (String) session.getAttribute("previousTicketsPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket ticket, Model model, RedirectAttributes redirectAttributes) {
        ticket.setTicketId(id);

        List<String> errorMessages = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("errorMessages",errorMessages);
        redirectAttributes.addFlashAttribute("successMessages",successMessages);

        try {
            ticket =  mTicketService.updateStatus(ticket);
        }
        catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/tickets/edit/" +id;
        }

        model.addAttribute("ticket",ticket);
        successMessages.add("Successfully Updated");
        return "redirect:/admin/tickets/edit/" +id;
    }



}