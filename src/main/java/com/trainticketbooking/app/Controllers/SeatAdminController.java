package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.ISeatService;
import com.trainticketbooking.app.Services.ISeatTypeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/seats")
public class SeatAdminController {
    @Autowired
    private ISeatService mSeatService;

    @Autowired
    private ISeatTypeService mSeatTypeService;

    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Seat> seatPage = mSeatService.findAll(pageRequest);

        model.addAttribute("seats", seatPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/seats/index?page=%d&size=%d", page, size);
        session.setAttribute("previousSeatsPage", currentUrl);

        return "admin/seats/index";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Seat> seatOpt = mSeatService.getById(id);

        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            model.addAttribute("seat", seat);
            model.addAttribute("ticketsOfSeat", seat.getTickets());
            return "admin/seats/edit";
        }
        return "redirect:" + (String) session.getAttribute("previousSeatsPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("seat") Seat seat, BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        seat.setSeatId(id);

        List<String> errorMessages = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("errorMessages",errorMessages);
        redirectAttributes.addFlashAttribute("successMessages",successMessages);

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return "redirect:/admin/seats/edit/" + id;
        }

        try {
            seat =  mSeatService.update(seat);
        }
        catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/seats/edit/" + id;
        }

        successMessages.add("Successfully Updated");
        return "redirect:/admin/seats/edit/" + id;
    }



    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes) {
        mSeatService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/seats/index";
    }

    @ModelAttribute("getAllSeatTypes")
    public List<SeatType> getAllSeatTypes() {
        return mSeatTypeService.getAll();
    }

}
