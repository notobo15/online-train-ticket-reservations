package com.trainticketbooking.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.trainticketbooking.app.Entities.Role;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Models.UserModel;
import com.trainticketbooking.app.Services.IRoleService;
import com.trainticketbooking.app.Services.IUserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "admin/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userModel") UserModel userModel, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/auth/register";
        }

        if (userService.findByUsername(userModel.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "admin/auth/register";
        }

        if (userService.findByEmail(userModel.getEmail()) != null) {
            model.addAttribute("error", "Email already exists!");
            return "admin/auth/register";
        }

        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setFullName(userModel.getFullName());

        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole != null) {
            user.setRole(adminRole);
        }

        userService.save(user);

        return "redirect:/login";
    }

}
