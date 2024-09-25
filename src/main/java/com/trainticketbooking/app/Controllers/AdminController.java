package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Station;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String homePage(Model model) {
        return "admin/index";
    }

    @GetMapping("train")
    public String trainIndex(Model model) {
        return "admin/trains/index";
    }

    @GetMapping("login")
    public String login(Model model) {
        return "admin/auth/login";
    }
}