package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import com.trainticketbooking.app.Services.IStationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/railway-network")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRailwayNetworkController {
    IRailwayNetworkService railwayNetworkService;
    IStationService stationService;

    @GetMapping({"", "/index"})
    public String getAllRailwayNetworks(Model model, Pageable pageable) {

        Page<RailwayNetwork> railwayNetworkPage = railwayNetworkService.findAll(pageable);

        model.addAttribute("railwayNetworks", railwayNetworkPage.getContent());
        model.addAttribute("currentPage", railwayNetworkPage.getNumber());
        model.addAttribute("totalPages", railwayNetworkPage.getTotalPages());
        model.addAttribute("size", railwayNetworkPage.getSize());

        return "admin/railway-network/index";
    }

    @GetMapping("/create")
    public String createRailwayNetwork(Model model) {
        model.addAttribute("railwayNetwork", new RailwayNetwork());
//        model.addAttribute("stations", stationService.getAll());
        return "admin/railway-network/create";
    }

//    @PostMapping("/create")
//    public String saveCreateUser(Model model, @ModelAttribute User user) {
//        try {
//            User userResponse = userService.save(user);
//            model.addAttribute("user", new User());
//            model.addAttribute(
//                    "successMessage",
//                    "User created successfully!");
//        } catch (Exception e) {
//            model.addAttribute(
//                    "errorMessage",
//                    "User created fail!  " + e.getMessage());
//        }
//        return "admin/users/create";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editUser(@PathVariable("id") Integer id, Model model) {
//        log.info("Start edit user");
//        try {
//            Optional<User> userOptional = userService.getById(id);
//            User user = userOptional.get();
//            model.addAttribute("user", user);
//        } catch (Exception e) {
//            model.addAttribute(
//                    "errorMessage",
//                    "User edited fail!  " + e.getMessage());
//        }
//        return "admin/users/edit";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String saveEditUser(@PathVariable("id") Integer id,
//                               @ModelAttribute User user,
//                               @RequestParam(value = "dob", required = false) LocalDate dob,
//                               Model model) {
//        log.info("Start save edit user");
//        user.setUserId(id);
//        if (dob != null) {
//            log.info("check dob: {}", dob.toString());
//            user.setDateOfBirth(dob);
//        }
//        try {
//            log.info("User update: {}", user.toString());
//            userService.adminUpdateUser(user);
//            model.addAttribute(
//                    "successMessage",
//                    String.format("Edited user with id = %d successfully!", user.getUserId())
//            );
//        } catch (Exception e) {
//            model.addAttribute(
//                    "errorMessage",
//                    "User edited fail!  " + e.getMessage());
//        }
////        model.addAttribute("user", user); ko cần dòng này vì @ModelAttribute sẽ tự add user vào model
//        return "admin/users/edit";
//    }
//
//    @GetMapping("detail/{id}")
//    public String viewDetailUser(@PathVariable("id") Integer id, Model model) {
//        log.info("Start detail user");
//        try {
//            Optional<User> userOptional = userService.getById(id);
//            User user = userOptional.get();
//            model.addAttribute("user", user);
//        } catch (Exception e) {
//            model.addAttribute(
//                    "errorMessage",
//                    "View detail user fail!  " + e.getMessage());
//        }
//        return "admin/users/detail";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            userService.deleteById(id);
//            redirectAttributes.addFlashAttribute(
//                    "successMessage",
//                    String.format("Delete user success with id = %d", id)
//            );
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute(
//                    "errorMessage",
//                    "Delete user fail!  " + e.getMessage());
//        }
//        return "redirect:/admin/users/index";
//    }
}
