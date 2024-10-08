package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Services.IRouteService;
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
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRoutesController {
    IRouteService routeService;

    @GetMapping({"", "/index"})
    public String getAllRoutes(Model model, Pageable pageable) {

        Page<Route> routePage = routeService.findAll(pageable);

        model.addAttribute("routes", routePage.getContent());
        model.addAttribute("currentPage", routePage.getNumber());
        model.addAttribute("totalPages", routePage.getTotalPages());
        model.addAttribute("size", routePage.getSize());

        return "admin/routes/index";
    }

    @GetMapping("/create")
    public String createRoute(Model model) {
        model.addAttribute("route", new Route());
        return "admin/routes/create";
    }

    @PostMapping("/create")
    public String saveCreateRoute(Model model, @ModelAttribute Route route) {
        try {
            Route userResponse = routeService.save(route);
            model.addAttribute("route", new Route());
            model.addAttribute(
                    "successMessage",
                    "Route created successfully!");
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Route created fail!  " + e.getMessage());
        }
        return "admin/routes/create";
    }
}
