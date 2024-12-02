package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.impl.CarriageService;
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
@RequestMapping("/admin/carriages")
public class CarriageAdminController {
    @Autowired
    private ICarriageService mCarriageService;

    @Autowired
    private ICarriageClassService mCarriageClassService;


    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Carriage> carriagePage = mCarriageService.findAll(pageRequest);

        model.addAttribute("carriages", carriagePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carriagePage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/carriages/index?page=%d&size=%d", page, size);
        session.setAttribute("previousCarriagesPage", currentUrl);

        return "admin/carriages/index";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Carriage> carOpt = mCarriageService.getById(id);
        int ticketsOfCarriage = mCarriageService.getTicketNumberOfCarriage(id);
        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        if (carOpt.isPresent()) {
            Carriage car = carOpt.get();
            model.addAttribute("car", car);
            model.addAttribute("errorMap", errorMap);
            model.addAttribute("successMessages", successMessages);
            model.addAttribute("ticketsOfCarriage", ticketsOfCarriage);
            return "admin/carriages/edit";
        }
        return (String) session.getAttribute("previousCarriagesPage");
    }

    @GetMapping("/{id}/tickets")
    public String getTickets(@PathVariable("id") Integer id, Model model) {
        List<Ticket> tickets = mCarriageService.getTicketsOfCarriage(id);
        Optional<Carriage> carOpt = mCarriageService.getById(id);

        if (carOpt.isPresent()) {
            model.addAttribute("tickets", tickets);
            model.addAttribute("car", carOpt.get());
            return "admin/carriages/tickets-of-carriage";
        }

        return "redirect:/admin/carriages/edit/" + id;
    }

    @GetMapping("/{id}/seats")
    public String getSeats(@PathVariable("id") Integer id, Model model) {
        List<Seat> seats = mCarriageService.getSeatsOfCarriage(id);
        Optional<Carriage> carOpt = mCarriageService.getById(id);

        if (carOpt.isPresent()) {
            model.addAttribute("seats", seats);
            model.addAttribute("car", carOpt.get());
            return "admin/carriages/seats-of-carriage";
        }

        return "redirect:/admin/carriages/edit/" + id;
    }
    
    @ModelAttribute("getAllCarriageClasses")
    public List<CarriageClass> getAllCarriageClasses() {
        return mCarriageClassService.getAll();
    }
//
//    @PostMapping("/edit/{id}")
//    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("cac") CarriageClass cac, BindingResult result, Model model) {
//        cac.setCarriageClassId(id);
//
//        Map<String, String> errorMap = new HashMap<>();
//        List<String> successMessages = new ArrayList<>();
//        model.addAttribute("errorMap", errorMap);
//        model.addAttribute("successMessages", successMessages);
//
//        if (result.hasErrors()) {
//            return "admin/carriage-classes/edit";
//        }
//
//        try {
//            cac = mCarriageClassService.update(cac);
//        } catch (RuntimeException ex) {
//            errorMap.put("generalError", ex.getMessage());
//            return "admin/carriage-classes/edit";
//        }
//
//        model.addAttribute("cac", cac);
//        successMessages.add("Successfully Updated");
//        return "admin/carriage-classes/edit";
//    }
//
//    @GetMapping("create")
//    public String create(Model model) {
//        CarriageClass cac = new CarriageClass();
//        Map<String, String> errorMap = new HashMap<>();
//
//        model.addAttribute("cac", cac);
//        model.addAttribute("errorMap", errorMap);
//        return "admin/carriage-classes/create";
//    }
//
//    @PostMapping("/store")
//    public String store(@Valid @ModelAttribute("cac") CarriageClass cac, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
//        Map<String, String> errorMap = new HashMap<>();
//        model.addAttribute("errorMap", errorMap);
//
//        if (result.hasErrors()) {
//            return "admin/carriage-classes/create";
//        }
//
//        try {
//            mCarriageClassService.save(cac);
//        } catch (RuntimeException ex) {
//            errorMap.put("generalError", ex.getMessage());
//            return "admin/carriage-classes/create";
//        }
//
//        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
//        return "redirect:/admin/carriage-classes/index";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
//        mCarriageClassService.deleteById(id);
//        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
//        return "redirect:/admin/carriage-classes/index";
//    }
}
