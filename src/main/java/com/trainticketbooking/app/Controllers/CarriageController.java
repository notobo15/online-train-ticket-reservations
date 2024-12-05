package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.ITrainService;
import com.trainticketbooking.app.Services.impl.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Slf4j
@Controller
@RequestMapping("/admin/carriages")
public class CarriageController {

    @Autowired
    private ICarriageService carriageService;

    @Autowired
    private ITrainService trainService;

    @Autowired
    private UserService userService;

    @Autowired
    private ICarriageClassService carriageClassService;

    @GetMapping({"", "/index"})
    public String getAllCarriages(Model model, Pageable pageable) {
        Page<Carriage> carriagePage = carriageService.findAll(pageable);

        model.addAttribute("carriages", carriagePage.getContent());
        model.addAttribute("currentPage", carriagePage.getNumber());
        model.addAttribute("totalPages", carriagePage.getTotalPages());
        model.addAttribute("size", carriagePage.getSize());

        return "admin/carriages/index";
    }

    @GetMapping("/create")
    public String createCarriage(Model model) {
        model.addAttribute("carriage", new Carriage());
        model.addAttribute("trains", trainService.getAll()); // Get all trains
//        model.addAttribute("carriageClasses", carriageClassService.getAllCarriageClasses()); // Assuming you have a service to fetch carriage classes

        return "admin/carriages/create";
    }

    @PostMapping("/create")
    public String saveCreateCarriage(Model model,
                                     @Valid @ModelAttribute Carriage carriage,
                                     BindingResult result) {
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(errorsJoiner::add);
            model.addAttribute("errorMessage", "Carriage creation failed! => " + errorsJoiner);
            return "admin/carriages/create";
        }
        try {
            Carriage carriageResponse = carriageService.save(carriage);
            model.addAttribute("carriage", new Carriage());
            model.addAttribute("successMessage", "Carriage created successfully with ID = " + carriageResponse.getCarriageId());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Carriage creation failed! " + e.getMessage());
        }
        return "admin/carriages/create";
    }



    @GetMapping("/edit/{id}")
    public String editCarriage(@PathVariable("id") Integer id, Model model) {
        Optional<Carriage> carriageOptional = carriageService.getById(id);
        if (carriageOptional.isPresent()) {
            model.addAttribute("carriage", carriageOptional.get());
            model.addAttribute("trains", trainService.getAll());
//            model.addAttribute("carriageClasses", carriageService.getAllCarriageClasses());
        } else {
            model.addAttribute("errorMessage", String.format("Carriage with ID = %d does not exist", id));
        }
        return "admin/carriages/edit";
    }

    @GetMapping("/{id}/seats")
    public String getSeats(@PathVariable("id") Integer id, Model model) {
        Optional<Carriage> carriageOptional = carriageService.getById(id);

        if (carriageOptional.isPresent()) {
            model.addAttribute("carriage", carriageOptional.get());
            List<Seat> seats = carriageOptional.get().getSeats();
            model.addAttribute("seats", seats);

            return "admin/trains/seats-of-carriage";
        }

        return "redirect:/admin/trains/edit/" + id;
    }



    @PostMapping("/edit/{id}")
    public String saveEditCarriage(@PathVariable("id") Integer id,
                                   Model model,
                                   @Valid @ModelAttribute Carriage carriage,
                                   BindingResult result) {
        carriage.setCarriageId(id);
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(errorsJoiner::add);
            model.addAttribute("errorMessage", "Carriage edit failed! => " + errorsJoiner);
            return "admin/carriages/edit";
        }
        try {
            Carriage carriageResponse = carriageService.save(carriage);
            model.addAttribute("successMessage", "Carriage edited successfully with ID = " + carriageResponse.getCarriageId());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Carriage edit failed! " + e.getMessage());
        }
        return "admin/carriages/edit";
    }


    @PostMapping("/carriages/delete/{id}")
    public String deleteCarriage(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            carriageService.deleteById(id);  // Assuming a service method to delete the carriage
            redirectAttributes.addFlashAttribute("successMessage", "Carriage deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete carriage: " + e.getMessage());
        }
        return "redirect:/admin/trains/edit/" + id;  // Redirect back to the edit train page
    }

    @GetMapping("/carriages/create")
    public String createCarriage(@RequestParam("trainId") Integer trainId, Model model) {
        Train train = trainService.getById(trainId).orElseThrow(() -> new RuntimeException("Train not found"));
        Carriage carriage = new Carriage();
        carriage.setTrain(train);
        model.addAttribute("train", train);
        model.addAttribute("carriage", carriage);
        model.addAttribute("carriageClasses", carriageClassService.getAll());  // Assuming you have a service to fetch all classes
        return "admin/carriages/create";
    }

    @PostMapping("/carriages/create")
    public String saveCarriage(@Valid @ModelAttribute Carriage carriage, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Failed to create carriage due to validation errors.");
            return "admin/carriages/create";
        }
        try {
            carriageService.save(carriage);  // Assuming a service method to save the carriage
            redirectAttributes.addFlashAttribute("successMessage", "Carriage added successfully.");
            return "redirect:/admin/trains/edit/" + carriage.getTrain().getTrainId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create carriage: " + e.getMessage());
            return "admin/carriages/create";
        }
    }

}
