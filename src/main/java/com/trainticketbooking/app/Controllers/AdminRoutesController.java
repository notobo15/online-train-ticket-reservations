package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Services.IRouteService;
import com.trainticketbooking.app.Services.IStationService;
import com.trainticketbooking.app.Services.ITrainService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.StringJoiner;

@Controller
@RequestMapping("/admin/routes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRoutesController {
    IRouteService routeService;
    ITrainService trainService;
    IStationService stationService;

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
        model.addAttribute("trains", trainService.getAll());
        model.addAttribute("stations", stationService.getAll());
        return "admin/routes/create";
    }

    @PostMapping("/create")
    public String saveCreateRoute(Model model,
                                  @Valid @ModelAttribute Route route,
                                  BindingResult result) {
        model.addAttribute("trains", trainService.getAll());
        model.addAttribute("stations", stationService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            log.error("Form submission has errors: {}", errorsJoiner);
            model.addAttribute(
                    "errorMessage",
                    "Route created fail! => " + errorsJoiner);
            return "admin/routes/create";
        }
        try {
            Route routeResponse = routeService.save(route);
            model.addAttribute("route", new Route());
            model.addAttribute(
                    "successMessage",
                    "Route created successfully with id = " + routeResponse.getRouteId());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Route created fail!  " + e.getMessage());
        }
        log.info("route request: {}", route.toString());
        return "admin/routes/create";
    }

    @GetMapping("/edit/{id}")
    public String editRoute(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit route");
        try {
            Optional<Route> routesOptional = routeService.getById(id);
            if (routesOptional.isPresent()) {
                Route route = routesOptional.get();
                model.addAttribute("route", route);
                model.addAttribute("trains", trainService.getAll());
                model.addAttribute("stations", stationService.getAll());
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("route has id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Route edited fail!  " + e.getMessage());
        }
        return "admin/routes/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditRoute(@PathVariable("id") Integer id,
                                Model model,
                                @Valid @ModelAttribute Route route,
                                BindingResult result) {
        route.setRouteId(id);
        model.addAttribute("trains", trainService.getAll());
        model.addAttribute("stations", stationService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Route edit fail! => " + errorsJoiner);
            return "admin/routes/edit";
        }
        try {
            Route routeResponse = routeService.save(route);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited route successfully with id = %d",
                            routeResponse.getRouteId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail!  " +
                            e.getMessage());
        }
        return "admin/routes/edit";
    }
}
