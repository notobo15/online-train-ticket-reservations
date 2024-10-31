package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Exceptions.CarriageNotFoundException;
import com.trainticketbooking.app.Services.ISeatService;
import com.trainticketbooking.app.Services.ISeatTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Seat> seatPage = mSeatService.findAll(pageRequest);

        model.addAttribute("seats", seatPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatPage.getTotalPages());
        model.addAttribute("size", size);
        return "admin/seats/index";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Optional<Seat> seatOpt = mSeatService.getById(id);
        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            model.addAttribute("seat", seat);
            model.addAttribute("train", seat.getCarriage().getTrain());
            model.addAttribute("tickets", seat.getTickets());

            return "admin/seats/detail";
        }
        return "admin/seats/index";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<Seat> seatOpt = mSeatService.getById(id);
        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            model.addAttribute("seat", seat);
            model.addAttribute("errorMap",errorMap);
            model.addAttribute("successMessages",successMessages);
            return "admin/seats/edit";
        }
        return "admin/seats/index";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("seat") Seat seat, BindingResult result, Model model) {
        seat.setSeatId(id);

        if (result.hasErrors()) {
            return "admin/seats/edit";
        }

        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        model.addAttribute("errorMap",errorMap);
        model.addAttribute("successMessages",successMessages);

        try {
          seat =  mSeatService.update(seat);
        }catch (CarriageNotFoundException ex) {
            errorMap.put("carriage", ex.getMessage());
            return "admin/seats/edit";
        }

        model.addAttribute("seat",seat);
        successMessages.add("Successfully Updated");
        return "admin/seats/edit";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        System.out.println("id xoa :" + id);
        mSeatService.deleteById(id);
        System.out.println("Seat deleted successfully");
        return "redirect:/admin/seats/index";
    }

    @ModelAttribute("getSeatTypes")
    public List<SeatType> getSeatTypes() {
        List<SeatType> seatTypes = mSeatTypeService.getAll();
        return seatTypes;
    }

}
