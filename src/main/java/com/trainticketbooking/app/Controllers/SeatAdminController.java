package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Exceptions.SeatTypeNotFoundException;
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

    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model,HttpSession session) {
        Optional<Seat> seatOpt = mSeatService.getById(id);
        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            model.addAttribute("seat", seat);
            return "admin/seats/detail";
        }
        return (String) session.getAttribute("previousSeatsPage");
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
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
        return (String) session.getAttribute("previousSeatsPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id,@Valid @ModelAttribute("seat") Seat seat, BindingResult result, Model model) {
        seat.setSeatId(id);

        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        model.addAttribute("errorMap",errorMap);
        model.addAttribute("successMessages",successMessages);

        if (result.hasErrors()) {
            return "admin/seats/edit";
        }

        try {
          seat =  mSeatService.update(seat);
        }catch (SeatTypeNotFoundException ex) {
            errorMap.put("seatType", ex.getMessage());
            return "admin/seats/edit";
        }
        catch (RuntimeException ex) {
            errorMap.put("generalError", ex.getMessage());
            return "admin/seats/edit";
        }

        model.addAttribute("seat",seat);
        successMessages.add("Successfully Updated");
        return "admin/seats/edit";
    }

    @GetMapping("create")
    public String create(Model model) {
        Seat seat = new Seat();
        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();

        model.addAttribute("seat",seat);
        model.addAttribute("errorMap",errorMap);
        model.addAttribute("successMessages",successMessages);
        return "admin/seats/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("seat") Seat seat, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Map<String, String> errorMap = new HashMap<>();
        model.addAttribute("errorMap", errorMap);

        if (result.hasErrors()) {
            return "admin/seats/create";
        }

        try {
            mSeatService.save(seat);
        } catch (SeatTypeNotFoundException ex) {
            errorMap.put("seatType", ex.getMessage());
            return "admin/seats/create";
        } catch (RuntimeException ex) {
            errorMap.put("generalError", ex.getMessage());
            return "admin/seats/create";
        }

        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
        return "redirect:/admin/seats/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes) {
        mSeatService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/seats/index";
    }

    @ModelAttribute("getSeatTypes")
    public List<SeatType> getSeatTypes() {
        return mSeatTypeService.getAll();
    }

}
