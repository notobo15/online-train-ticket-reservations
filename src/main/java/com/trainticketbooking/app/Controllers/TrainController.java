package com.trainticketbooking.app.Controllers;

import java.util.*;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.*;
import com.trainticketbooking.app.Services.impl.CarriageService;
import com.trainticketbooking.app.Services.impl.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.trainticketbooking.app.Entities.Train;
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
    public String saveCreateTrain(Model model,
                                  @Valid @ModelAttribute Train train,
                                  BindingResult result) {
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Train created fail! => " + errorsJoiner);
            return "admin/trains/create";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute("train", new Train());
            model.addAttribute(
                    "successMessage",
                    "Train created successfully with id = " + trainResponse.getTrainId());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Train created fail!  " + e.getMessage());
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trains/create";
    }

    @GetMapping("/edit/{id}")
    public String editTrain(@PathVariable("id") Integer id, Model model) {
        try {
            Optional<Train> trainOptional = trainService.getById(id);
            if (trainOptional.isPresent()) {
                Train train = trainOptional.get();
                model.addAttribute("train", train);
                model.addAttribute("railwayNetworks", railwayNetworkService.getAll());

                List<Carriage> carriages = train.getCarriages();
                Collections.sort(carriages, Comparator.comparingInt(Carriage::getOrderNumber));

                model.addAttribute("carriages", carriages);  // Add carriages to the model
                model.addAttribute("carriageClasses", carriageClassService.getAll());  // Add carriages to the model

                model.addAttribute("stations", stationService.getAll());
                model.addAttribute("routes", train.getRoutes());

                model.addAttribute("route", new Route());
            } else {
                model.addAttribute("errorMessage", String.format("Train with ID = %d does not exist", id));
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to load train data: " + e.getMessage());
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "admin/trains/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditTrain(@PathVariable("id") Integer id,
                                Model model,
                                @Valid @ModelAttribute Train train,
                                BindingResult result) {
        train.setTrainId(id);
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Train edit fail! => " + errorsJoiner);
            return "admin/trains/edit";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited train successfully with id = %d",
                            trainResponse.getTrainId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Edited train fail!  " +
                            e.getMessage());
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
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete train success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete train fail!  " + e.getMessage());
        }

        return "redirect:/admin/trains/index";
    }

    @PostMapping("/carriages/create")
    public String createCarriage(@RequestParam("trainId") Integer trainId, @RequestParam("carriageClassId") Integer carriageClassId, @Valid @ModelAttribute Carriage carriage,
                                 BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Failed to create carriage due to validation errors.");
            return "admin/trains/edit";  // Assuming you're submitting the form from the train edit page
        }
        try {
            var carriageClass = carriageClassService.getById(carriageClassId).orElseThrow(() -> new RuntimeException("CarriageClass not found"));
            carriage.setCarriageClass(carriageClass);
            carriage.setTrain(trainService.getById(trainId).orElseThrow(() -> new RuntimeException("Train not found")));
            carriageService.save(carriage);
            redirectAttributes.addFlashAttribute("successMessage", "Carriage added successfully.");
            return "redirect:/admin/trains/edit/" + trainId;  // Redirect back to the same page after adding the carriage
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create carriage: " + e.getMessage());
            return "redirect:/admin/trains/edit/" + trainId;
        }
    }


    @PostMapping("/carriages/delete/{id}")
    public String deleteCarriage(@PathVariable("id") Integer id,@RequestParam("trainId") Integer trainId, RedirectAttributes redirectAttributes) {
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
    public String createRoute(@Valid @ModelAttribute("route") Route route, BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // If there are validation errors, return the form with error messages
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create route due to validation errors.");
            return "redirect:/admin/routes/create";
        }
        try {
            // Save the route (assuming routeService handles the database persistence)
            routeService.save(route);
            redirectAttributes.addFlashAttribute("successMessage", "Route created successfully!");
            return "redirect:/admin/routes";  // Redirect to the route list page
        } catch (Exception e) {
            // Handle any errors that occur during route creation
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create route: " + e.getMessage());
            return "redirect:/admin/routes/create";
        }
    }
}
