package com.trainticketbooking.app.Controllers;

import java.util.*;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Services.*;
import com.trainticketbooking.app.Services.impl.CarriageService;
import com.trainticketbooking.app.Services.impl.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/trains")
public class TrainController {

    @Autowired
    private ITrainService trainService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private ICarriageClassService carriageClassService;

    @Autowired
    private IStationService stationService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IRailwayNetworkService railwayNetworkService;

    private Integer trainId;

    @GetMapping({"", "/index"})
    public String getAllTrains(Model model, Pageable pageable) {
        Page<Train> trainPage = trainService.findAll(pageable);

        model.addAttribute("trains", trainPage.getContent());
        model.addAttribute("currentPage", trainPage.getNumber());
        model.addAttribute("totalPages", trainPage.getTotalPages());
        model.addAttribute("size", trainPage.getSize());

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trains/index";
    }

    @GetMapping("/create")
    public String createTrain(Model model) {
        model.addAttribute("train", new Train());
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "admin/trains/create";
    }

    @PostMapping("/create")
    public String saveCreateTrain(Model model, @Valid @ModelAttribute Train train, BindingResult result) {
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute("errorMessage", "Train created fail! => " + errorsJoiner);
            return "admin/trains/create";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute("train", new Train());
            model.addAttribute("successMessage", "Train created successfully with id = " + trainResponse.getTrainId());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Train created fail!  " + e.getMessage());
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trains/create";
    }

    @GetMapping("/edit/{id}")
    public String editTrain(@PathVariable("id") Integer id, Model model) {
        trainId = id;
        try {
            Optional<Train> trainOptional = trainService.getById(id);
            if (trainOptional.isPresent()) {
                Train train = trainOptional.get();
                model.addAttribute("train", train);
                model.addAttribute("railwayNetworks", railwayNetworkService.getAll());

                List<Carriage> carriages = train.getCarriages();
                carriages.sort(Comparator.comparing(carriage -> carriage.getOrderNumber() != null ? carriage.getOrderNumber() : Integer.MAX_VALUE));

                model.addAttribute("carriages", carriages);  // Add carriages to the model
                model.addAttribute("carriageClasses", carriageClassService.getAll());  // Add carriages to the model

                model.addAttribute("stations", stationService.getAll());
                model.addAttribute("routes", train.getRoutes());
                Carriage carriage = new Carriage();
                model.addAttribute("route", new Route());
                model.addAttribute("carriage", carriage);  // Add carriages to the model

            } else {
                model.addAttribute("errorMessage", String.format("Train with ID = %d does not exist", id));
            }
        } catch (Exception e) {
            e.printStackTrace(); // In chi tiết lỗi ra console để gỡ lỗi
            model.addAttribute("errorMessage", "Exception occurred: " + e.toString());
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "admin/trains/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditTrain(@PathVariable("id") Integer id, Model model, @Valid @ModelAttribute Train train, BindingResult result) {
        train.setTrainId(id);
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute("errorMessage", "Train edit fail! => " + errorsJoiner);
            return "admin/trains/edit";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute("successMessage", String.format("Edited train successfully with id = %d", trainResponse.getTrainId()));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Edited train fail!  " + e.getMessage());
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trains/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            trainService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", String.format("Delete train success with id = %d", id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Delete train fail!  " + e.getMessage());
        }

        return "redirect:/admin/trains/index";
    }

    @PostMapping("/carriages/create")
    public String saveCreateCarriage(@RequestParam("trainId") Integer trainId, @RequestParam("carriageClassId") Integer carriageClassId, @Valid @ModelAttribute Carriage carriage,
                                     BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Failed to create carriage due to validation errors.");
            return "admin/trains/edit";  // Assuming you're submitting the form from the train edit page
        }
        try {
            var carriageClass = carriageClassService.getById(carriageClassId).orElseThrow(() -> new RuntimeException("CarriageClass not found"));
            carriage.setCarriageClass(carriageClass);

            var train = trainService.getById(trainId).orElseThrow(() -> new RuntimeException("Train not found"));

            carriage.setTrain(train);

            int orderNumber = 0;

            var carriages = carriageService.findByTrainTrainId(trainId);
            if (!carriages.isEmpty()) {

                Carriage lastCarriage = carriages.stream()
                        .max(Comparator.comparingInt(Carriage::getOrderNumber))
                        .orElseThrow(() -> new RuntimeException("Failed to find the last carriage"));

                orderNumber = lastCarriage.getOrderNumber() + 1;
            }

            carriage.setOrderNumber(orderNumber);

            carriageService.save(carriage);

            redirectAttributes.addFlashAttribute("successMessage", "Carriage added successfully.");
            return "redirect:/admin/trains/edit/" + trainId;
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, thêm thông báo lỗi và quay lại trang chỉnh sửa
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create carriage: " + e.getMessage());
            return "redirect:/admin/trains/edit/" + trainId;
        }
    }

    @GetMapping("/carriages/edit/{id}")
    public String editCarriage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("carriageClasses", carriageClassService.getAll());
        try {
            Optional<Carriage> carriageOptional = carriageService.getById(id);
            if (carriageOptional.isPresent()) {
                Carriage carriage = carriageOptional.get();
                model.addAttribute("carriage", carriage);
            } else {
                model.addAttribute("errorMessage", String.format("carriage with ID = %d does not exist", id));
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Exception occurred editCarriage: " + e.toString());
        }
        return "admin/trains/editCarriage";
    }

    @PostMapping("/carriages/edit/{id}")
    public String saveEditCarriage(@PathVariable("id") Integer id, Model model, @Valid @ModelAttribute Carriage carriageRequest, BindingResult result, RedirectAttributes redirectAttributes) {
        model.addAttribute("carriageClasses", carriageClassService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute("errorMessage", "carriage edit fail! => " + errorsJoiner);
            return "admin/trains/editCarriage";
        }
        try {
            Optional<Carriage> carriageOptional = carriageService.getById(id);
            if (carriageOptional.isPresent()) {
                Carriage carriage = carriageOptional.get();
                carriage.setCarriageClass(carriageRequest.getCarriageClass());
                carriage.setCarriageNumber(carriageRequest.getCarriageNumber());

                Carriage carriageResponse = carriageService.save(carriage);
                redirectAttributes.addFlashAttribute("successMessage", String.format("Edited Carriage successfully with id = %d", carriageResponse.getCarriageId()));
                return "redirect:/admin/trains/edit/" + trainId;
            } else {
                model.addAttribute("errorMessage", String.format("carriage with ID = %d does not exist", id));
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Edited Carriage fail!  " + e.getMessage());
        }
        return "admin/trains/editCarriage";
    }

    @PostMapping("/carriages/delete/{id}")
    public String deleteCarriage(@PathVariable("id") Integer id, @RequestParam("trainId") Integer trainId, RedirectAttributes redirectAttributes) {
        try {
            carriageService.deleteById(id);  // Assuming a service method to delete the carriage
            redirectAttributes.addFlashAttribute("successMessage", "Carriage deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete carriage: " + e.getMessage());
        }
        return "redirect:/admin/trains/edit/" + trainId;  // Redirect back to the edit train page
    }

    @PostMapping("/carriages/update-order")
    @ResponseBody
    public ResponseEntity<?> updateCarriageOrder(@RequestBody Map<String, List<Map<String, Object>>> payload) {
        List<Map<String, Object>> order = payload.get("order");

        try {
            // Duyệt qua order và cập nhật thứ tự trong cơ sở dữ liệu
            for (Map<String, Object> entry : order) {
                Integer carriageId = (Integer) entry.get("id");
                Integer orderNumber = (Integer) entry.get("order");

                Optional<Carriage> carriage = carriageService.getById(carriageId);
                if (carriage.isPresent()) {
                    carriage.get().setOrderNumber(orderNumber);
                    carriageService.save(carriage.orElse(null));
                }
            }
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }

    @PostMapping("/routes/create")
    public String saveCreateRoute(@Valid @ModelAttribute("route") Route route, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            redirectAttributes.addFlashAttribute("errorMessage", "Route created fail! => " + errorsJoiner);
            return "redirect:/admin/trains/edit/" + trainId;
        }
        try {
            Optional<Train> trainOptional = trainService.getById(trainId);
            if (trainOptional.isPresent()) {
                Train train = trainOptional.get();
                route.setTrain(train);
                Route routeResponse = routeService.save(route);
                redirectAttributes.addFlashAttribute("successMessage", "Route created successfully with id = " + routeResponse.getRouteId());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", String.format("train with ID = %d does not exist", trainId));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Route created fail!  " + e.toString());
        }
        return "redirect:/admin/trains/edit/" + trainId;
    }

    @GetMapping("/routes/edit/{id}")
    public String editRoute(@PathVariable("id") Integer id, Model model) {
        try {
            Optional<Route> routesOptional = routeService.getById(id);
            if (routesOptional.isPresent()) {
                Route route = routesOptional.get();
                model.addAttribute("route", route);
                model.addAttribute("stations", stationService.getAll());
                return "admin/trains/editRoute";
            } else {
                model.addAttribute("errorMessage", String.format("route has id = %d does not exist", id));
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Route edited fail!  " + e.toString());
        }
        return "redirect:/admin/trains/edit/" + trainId;
    }

    @PostMapping("/routes/edit/{id}")
    public String saveEditRoute(@PathVariable("id") Integer id, Model model, @Valid @ModelAttribute Route route, BindingResult result, RedirectAttributes redirectAttributes) {
        model.addAttribute("stations", stationService.getAll());
        route.setRouteId(id);
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute("errorMessage", "route edit fail! => " + errorsJoiner);
            return "admin/trains/editRoute";
        }
        try {
            Optional<Train> trainOptional = trainService.getById(trainId);
            if (trainOptional.isPresent()) {
                Train train = trainOptional.get();
                route.setTrain(train);
                Route routeResponse = routeService.save(route);
                redirectAttributes.addFlashAttribute("successMessage", String.format("Edited Route successfully with id = %d", routeResponse.getRouteId()));
                return "redirect:/admin/trains/edit/" + trainId;
            } else {
                model.addAttribute("errorMessage", String.format("Train has id = %d does not exist", trainId));
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Edited Route fail!  " + e.toString());
        }
        return "admin/trains/editRoute";
    }

    @PostMapping("/routes/delete/{id}")
    public String deleteRoute(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            routeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Route deleted successfully with id = " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete Route: " + e.toString());
        }
        return "redirect:/admin/trains/edit/" + trainId;
    }
}
