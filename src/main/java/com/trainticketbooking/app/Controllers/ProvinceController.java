package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Models.UserModel;
import com.trainticketbooking.app.Services.IProvinceService;
import com.trainticketbooking.app.Services.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin/provinces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProvinceController {
    IUserService userService;
    IProvinceService provinceService;

    @GetMapping({"", "/index"})
    public String getAllProvinces(Model model, Pageable pageable) {

        Page<Province> provincesPage = provinceService.findAll(pageable);

        model.addAttribute("provinces", provincesPage.getContent());
        model.addAttribute("currentPage", provincesPage.getNumber());
        model.addAttribute("totalPages", provincesPage.getTotalPages());
        model.addAttribute("size", provincesPage.getSize());

        return "admin/provinces/index";
    }

    @GetMapping("/create")
    public String createProvince(Model model) {
        model.addAttribute("province", new Province());
        return "admin/provinces/create";
    }

    @PostMapping("/create")
    public String saveCreateProvince(Model model, @ModelAttribute Province province) {
        try {
            Province provinceResponse = provinceService.save(province);
            model.addAttribute("province", new Province());
            model.addAttribute(
                    "successMessage",
                    "Province created successfully!");
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Province created fail!  " + e.getMessage());
        }
        return "admin/provinces/create";
    }

    @GetMapping("/edit/{id}")
    public String editProvince(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit province");
        try {
            Optional<Province> provinceOptional = provinceService.getById(id);
            Province province = provinceOptional.get();
            model.addAttribute("province", province);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Province edited fail!  " + e.getMessage());
        }
        return "admin/provinces/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditProvince(@PathVariable("id") Integer id,
                               @ModelAttribute Province province,
                               Model model) {
        log.info("Start save edit province");
        province.setProvinceId(id);

        try {
            log.info("Province update: {}", province.toString());
            provinceService.adminUpdateProvince(province);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited province with id = %d successfully!", province.getProvinceId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Province edited fail!  " + e.getMessage());
        }
//        model.addAttribute("user", user); ko cần dòng này vì @ModelAttribute sẽ tự add user vào model
        return "admin/provinces/edit";
    }

    @GetMapping("detail/{id}")
    public String viewDetailUser(@PathVariable("id") Integer id, Model model) {
        log.info("Start detail province");
        try {
            Optional<Province> provinceOptional = provinceService.getById(id);
            Province province = provinceOptional.get();
            model.addAttribute("province", province);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "View detail province fail!  " + e.getMessage());
        }
        return "admin/provinces/detail";
    }
git
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            provinceService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete user success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete user fail!  " + e.getMessage());
        }
        return "redirect:/admin/provinces/index";
    }

}
