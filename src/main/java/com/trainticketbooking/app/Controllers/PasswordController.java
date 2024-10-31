package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.ResetToken;
import com.trainticketbooking.app.Models.ResetPassword;
import com.trainticketbooking.app.Services.impl.UserService;
import com.trainticketbooking.app.Services.impl.EmailService;
import com.trainticketbooking.app.Entities.User;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Controller
public class PasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("forgot-password")
    public String showForgotPasswordPage() {
        return "admin/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Email address not found.");
            return "admin/auth/forgot-password";
        }

        String token = userService.generateResetToken(user);

        try {
            emailService.sendResetPasswordEmail(user.getEmail(), token);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending reset password email: " + e.getMessage());
            model.addAttribute("error", "Failed to send reset password email.");
            return "admin/auth/forgot-password";
        }

        model.addAttribute("message", "A reset password link has been sent to your email.");
        return "admin/auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        ResetToken resetToken = userService.findByResetToken(token);
        if (resetToken == null) {
            model.addAttribute("error", "The reset token is invalid. Please request a new password reset.");
            return "admin/auth/reset-password";
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "The reset token has expired. Please request a new password reset.");
            return "admin/auth/reset-password";
        }

        model.addAttribute("resetPassword", new ResetPassword());
        model.addAttribute("token", token);
        return "admin/auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute("resetPassword") @Valid ResetPassword resetPassword,
                                       BindingResult bindingResult,
                                       Model model) {

        ResetToken resetToken = userService.findByResetToken(resetPassword.getToken());
        if (resetToken == null) {
            model.addAttribute("error", "The reset token is invalid. Please request a new password reset.");
            return "admin/auth/reset-password";
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            model.addAttribute("error", "The reset token has expired. Please request a new password reset.");
            return "admin/auth/reset-password";
        }

        if (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("token", resetPassword.getToken());
            return "admin/auth/reset-password";
        }

        User user = resetToken.getUser();
        userService.updatePassword(user, resetPassword.getPassword());
        userService.deleteResetToken(resetPassword.getToken());

        model.addAttribute("message", "Your password has been reset successfully.");
        return "redirect:/login";
    }
}