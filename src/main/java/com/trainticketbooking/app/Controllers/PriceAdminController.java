package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Entities.Price;
import com.trainticketbooking.app.Entities.SeatType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/prices")
public class PriceAdminController {

    @Autowired
    private IPriceService mPriceService;

    @GetMapping({"","/", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Price> pricePage = mPriceService.findAll(pageRequest);

        model.addAttribute("prices", pricePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pricePage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/prices/index?page=%d&size=%d", page, size);
        session.setAttribute("previousPricesPage", currentUrl);

        return "admin/prices/index";
    }


    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Price> priceOpt = mPriceService.getById(id);

        if (priceOpt.isPresent()) {
            Price price = priceOpt.get();


            model.addAttribute("price", price);
            return "admin/prices/edit";
        }
        return "redirect:" + session.getAttribute("previousPricesPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("price") Price price, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        price.setPriceId(id);

        List<String> errorMessages = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("successMessages", successMessages);
        redirectAttributes.addFlashAttribute("errorMessages", errorMessages);

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return "redirect:/admin/prices/edit/" + id;
        }

        try {
            price = mPriceService.update(price);
        } catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/prices/edit/" + id;
        }

        successMessages.add("Successfully Updated");
        return "redirect:/admin/prices/edit/" + id;
    }

    @GetMapping("/create")
    public String create(Model model) {
        Price price = new Price();

        model.addAttribute("price", price);
        return "admin/prices/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("price") Price price, BindingResult result, RedirectAttributes redirectAttributes) {
        List<String> errorMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("errorMessages", errorMessages);

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return "redirect:/admin/prices/create";
        }

        try {
            price = mPriceService.save(price);
        } catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/admin/prices/create" ;
        }

        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
        return "redirect:/admin/prices/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        mPriceService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/prices/index";
    }
}