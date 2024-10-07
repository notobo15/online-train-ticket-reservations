package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.User;
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

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminUsersController {
    IUserService userService;

    @GetMapping({"", "/index"})
    public String getAllUsers(Model model, Pageable pageable) {

        Page<User> userPage = userService.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", userPage.getSize());

        return "admin/users/index";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create";
    }

    @PostMapping("/create")
    public String saveCreateUser(Model model, @ModelAttribute User user) {
        try {
            User userResponse = userService.save(user);
            model.addAttribute("user", new User());
            model.addAttribute(
                    "successMessage",
                    "User created successfully!");
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "User created fail!  " + e.getMessage());
        }
        return "admin/users/create";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit user");
        try {
            Optional<User> userOptional = userService.getById(id);
            User user = userOptional.get();
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "User edited fail!  " + e.getMessage());
        }
        return "admin/users/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditUser(@PathVariable("id") Integer id,
                               @ModelAttribute User user,
                               @RequestParam(value = "dob", required = false) LocalDate dob,
                               Model model) {
        log.info("Start save edit user");
        user.setUserId(id);
        if (dob != null) {
            log.info("check dob: {}", dob.toString());
            user.setDateOfBirth(dob);
        }
        try {
            log.info("User update: {}", user.toString());
            userService.adminUpdateUser(user);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited user with id = %d successfully!", user.getUserId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "User edited fail!  " + e.getMessage());
        }
//        model.addAttribute("user", user);
        return "admin/users/edit";
    }

//    @GetMapping("detail/{id}")
//    public String getDetails(@PathVariable("id") Integer id, Model model) {
//        Optional<Train> train = trainService.getById(id);
//        if (train.isPresent()) {
//            model.addAttribute("train", train.get());
//            return "admin/trains/detail";
//        }
//        return "admin/trains/index";
//    }

}
