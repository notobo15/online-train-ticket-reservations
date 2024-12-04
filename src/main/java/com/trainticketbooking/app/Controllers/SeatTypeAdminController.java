package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Price;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Services.ISeatTypeService;
import com.trainticketbooking.app.Services.IPriceService;
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
@RequestMapping("/admin/seat-types")
public class SeatTypeAdminController {

    @Autowired
    private ISeatTypeService mSeatTypeService;

    @Autowired
    private IPriceService mPriceService;

    @GetMapping({"","/", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<SeatType> seatTypePage = mSeatTypeService.findAll(pageRequest);

        model.addAttribute("seatTypes", seatTypePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", seatTypePage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/seat-types/index?page=%d&size=%d", page, size);
        session.setAttribute("previousSeatTypesPage", currentUrl);

        return "admin/seat-types/index";
    }


    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<SeatType> seatTypeOpt = mSeatTypeService.getById(id);

        if (seatTypeOpt.isPresent()) {
            SeatType seatType = seatTypeOpt.get();

            // Lấy danh sách price chưa được sử dụng và price hiện tại của seat type này
            List<Price> availablePrices = mPriceService.findAvailablePrices();
            if (seatType.getPrice() != null) {
                availablePrices.add(seatType.getPrice()); // Thêm price hiện tại vào danh sách để có thể chọn lại
            }

            model.addAttribute("st", seatType);
            model.addAttribute("availablePrices", availablePrices);
            return "admin/seat-types/edit";
        }
        return "redirect:" + session.getAttribute("previousSeatTypesPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("st") SeatType seatType, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        seatType.setSeatTypeId(Integer.toUnsignedLong(id));

        List<String> errorMessages = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("successMessages", successMessages);
        redirectAttributes.addFlashAttribute("errorMessages", errorMessages);

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return "redirect:/admin/seat-types/edit/" + id;
        }

        try {
            seatType = mSeatTypeService.update(seatType);
        } catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/seat-types/edit/" + id;
        }

        successMessages.add("Successfully Updated");
        return "redirect:/admin/seat-types/edit/" + id;
    }

    @GetMapping("/create")
    public String create(Model model) {
        SeatType st = new SeatType();

        // Lấy danh sách price chưa được sử dụng
        List<Price> availablePrices = mPriceService.findAvailablePrices();
        model.addAttribute("availablePrices", availablePrices);
        model.addAttribute("st", st);

        return "admin/seat-types/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("st") SeatType seatType, BindingResult result, RedirectAttributes redirectAttributes) {
        List<String> errorMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("errorMessages", errorMessages);

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return "redirect:/admin/seat-types/create";
        }

        try {
            seatType = mSeatTypeService.save(seatType);
        } catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/seat-types/create" ;
        }

        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
        return "redirect:/admin/seat-types/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        mSeatTypeService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/seat-types/index";
    }

}