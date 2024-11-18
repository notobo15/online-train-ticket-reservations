package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.IRoleService;
import com.trainticketbooking.app.Services.IUserService;
import com.trainticketbooking.app.Validations.ValidationGroups;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;
import java.util.StringJoiner;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminUsersController {
    IUserService userService;
    IRoleService roleService;

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
    public String saveCreateUser(Model model,
                                 @Validated(ValidationGroups.onCreate.class) @ModelAttribute User user,
                                 BindingResult result) {
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "User created fail! => " + errorsJoiner);
            return "admin/users/create";
        }
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
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
                model.addAttribute("roles", roleService.getAll());
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("user has id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "User edited fail!  " + e.getMessage());
        }
        return "admin/users/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditUser(@PathVariable("id") Integer id,
                               @Validated(ValidationGroups.onUpdate.class) @ModelAttribute User user,
                               BindingResult result,
                               @RequestParam(value = "dob", required = false) LocalDate dob,
                               Model model) {
        user.setUserId(id);
        model.addAttribute("roles", roleService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "User edited fail! => " + errorsJoiner);
            return "admin/users/edit";
        }
        log.info("Start save edit user");
        if (dob != null) {
            log.info("check dob: {}", dob.toString());
            user.setDateOfBirth(dob);
        }
        log.info("getRole: {}", user.getRole());
        log.info("getRoleId: {}", user.getRole().getRoleId());
        if (user.getRole().getRoleId() == null) {
            user.setRole(null);
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
        return "admin/users/edit";
    }

    @GetMapping("detail/{id}")
    public String viewDetailUser(@PathVariable("id") Integer id, Model model) {
        log.info("Start detail user");
        try {
            Optional<User> userOptional = userService.getById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("user id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "View detail user fail!  " + e.getMessage());
        }
        return "admin/users/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete user success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete user fail!  " + e.getMessage());
        }
        return "redirect:/admin/users/index";
    }

}
