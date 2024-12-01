package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Dtos.ChangePasswordForm;
import com.trainticketbooking.app.Dtos.UserDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
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
    @GetMapping("/profile")
    public String getProfile(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        model.addAttribute("updateInfoForm", userService.findByUsername(currentUser.getUsername()));
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "admin/users/profile";
    }

    // POST 1: Cập nhật thông tin người dùng (Email, Full Name, Address, Phone)
    @PostMapping("/update-profile-info")
    public String updateProfileInfo(@ModelAttribute User user, Model model) {
        // Cập nhật thông tin người dùng

        userService.updateProfileInfo(user);
        model.addAttribute("successMessage", "Profile information updated successfully.");
        return "redirect:/admin/users/profile";  // Redirect về trang profile
    }

    // POST 2: Cập nhật hình ảnh người dùng
    @PostMapping("/update-profile-image")
    public String updateProfileImage(@RequestParam("profileImage") MultipartFile profileImage, Model model) {
        if (profileImage.isEmpty()) {
            model.addAttribute("errorMessage", "Please select an image to upload.");
            return "redirect:/admin/users/profile";
        }
        UserDto currentUser = userService.getCurrentUserDto();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        // Cập nhật hình ảnh người dùng
        userService.saveProfileImage(profileImage);
        model.addAttribute("successMessage", "Profile image updated successfully.");
        return "redirect:/admin/users/profile";
    }

    // POST 3: Cập nhật mật khẩu
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/admin/users/profile";
        }

        // Cập nhật mật khẩu
        userService.updatePassword(password);
        model.addAttribute("successMessage", "Password updated successfully.");
        return "redirect:/admin/users/profile";
    }

}
