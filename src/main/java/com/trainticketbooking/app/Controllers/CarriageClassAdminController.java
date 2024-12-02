package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Exceptions.SeatTypeNotFoundException;
import com.trainticketbooking.app.Services.ICarriageClassService;
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
@RequestMapping("/admin/carriage-classes")
public class CarriageClassAdminController {
    @Autowired
    private ICarriageClassService mCarriageClassService;


    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<CarriageClass> carriageClassPage = mCarriageClassService.findAll(pageRequest);

        model.addAttribute("carriageClasses", carriageClassPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carriageClassPage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/carriage-classes/index?page=%d&size=%d", page, size);
        session.setAttribute("previousCarriageClassesPage", currentUrl);

        return "admin/carriage-classes/index";
    }

    @GetMapping("detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<CarriageClass> cacOpt = mCarriageClassService.getById(id);
        if (cacOpt.isPresent()) {
            CarriageClass cac = cacOpt.get();
            model.addAttribute("cac", cac);
            return "admin/carriage-classes/detail";
        }
        return (String) session.getAttribute("previousCarriageClassesPage");
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<CarriageClass> cacOpt = mCarriageClassService.getById(id);

        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        if (cacOpt.isPresent()) {
            CarriageClass cac = cacOpt.get();
            model.addAttribute("cac", cac);
            model.addAttribute("errorMap", errorMap);
            model.addAttribute("successMessages", successMessages);
            return "admin/carriage-classes/edit";
        }
        return (String) session.getAttribute("previousCarriageClassesPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("cac") CarriageClass cac, BindingResult result, Model model) {
        cac.setCarriageClassId(id);

        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        model.addAttribute("errorMap", errorMap);
        model.addAttribute("successMessages", successMessages);

        if (result.hasErrors()) {
            return "admin/carriage-classes/edit";
        }

        try {
            cac = mCarriageClassService.update(cac);
        } catch (RuntimeException ex) {
            errorMap.put("generalError", ex.getMessage());
            return "admin/carriage-classes/edit";
        }

        model.addAttribute("cac", cac);
        successMessages.add("Successfully Updated");
        return "admin/carriage-classes/edit";
    }

    @GetMapping("create")
    public String create(Model model) {
        CarriageClass cac = new CarriageClass();
        Map<String, String> errorMap = new HashMap<>();

        model.addAttribute("cac", cac);
        model.addAttribute("errorMap", errorMap);
        return "admin/carriage-classes/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("cac") CarriageClass cac, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Map<String, String> errorMap = new HashMap<>();
        model.addAttribute("errorMap", errorMap);

        if (result.hasErrors()) {
            return "admin/carriage-classes/create";
        }

        try {
            mCarriageClassService.save(cac);
        } catch (RuntimeException ex) {
            errorMap.put("generalError", ex.getMessage());
            return "admin/carriage-classes/create";
        }

        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
        return "redirect:/admin/carriage-classes/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        mCarriageClassService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/carriage-classes/index";
    }

}
