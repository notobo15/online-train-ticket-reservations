package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.TrainJourney;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.impl.TrainJourneyService;
import com.trainticketbooking.app.Services.impl.TrainService;
import com.trainticketbooking.app.Services.impl.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/trainjourneys")
public class TrainJourneyController {

    @Autowired
    private TrainJourneyService trainJourneyService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private UserService userService;

    @GetMapping({"", "/index"})
    public String getAllTrainJourneys(Model model) {
        List<TrainJourney> journeys = trainJourneyService.getAll();
        model.addAttribute("journeys", journeys);
        model.addAttribute("trains", trainService.getAll()); // List all trains for selection
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trainjourneys/index";  // This view will list all journeys
    }

    @GetMapping("/create")
    public String createTrainJourney(Model model) {
        model.addAttribute("trainJourney", new TrainJourney());
        model.addAttribute("trains", trainService.getAll());  // Provide all trains for selection

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }
        return "admin/trainjourneys/create";  // This view will allow creating new journeys
    }

    @PostMapping("/create")
    public String saveTrainJourney(@Valid @ModelAttribute TrainJourney trainJourney,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/trainjourneys/create";  // If validation fails, return to form
        }

        try {
            // Saving the new Train Journey
            trainJourneyService.save(trainJourney);
            redirectAttributes.addFlashAttribute("successMessage", "Train journey created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create train journey: " + e.getMessage());
        }
        return "redirect:/admin/trainjourneys/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrainJourney(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            trainJourneyService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Train journey deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete train journey: " + e.getMessage());
        }
        return "redirect:/admin/trainjourneys/index";
    }

//    @PostMapping("/create")
//    public String saveTrainJourney(@RequestParam("selectedTrains") List<Integer> selectedTrainIds,
//                                   @RequestParam("departureDate") String departureDateStr,
//                                   RedirectAttributes redirectAttributes) {
//        try {
//            LocalDate departureDate = LocalDate.parse(departureDateStr);
//
//            // Lưu hành trình tàu cho các tàu được chọn
//            for (Integer trainId : selectedTrainIds) {
//                // Lấy tàu tương ứng từ ID và lưu hành trình
//                Train train = trainService.getById(trainId).orElseThrow(() -> new RuntimeException("Train not found"));
//                TrainJourney trainJourney = new TrainJourney();
//                trainJourney.setTrain(train);
//                trainJourney.setDepartureDate(departureDate);
//                trainJourney.setStatus("Upcoming");  // Mặc định trạng thái là 'Upcoming'
//                trainJourneyService.save(trainJourney);
//            }
//
//            redirectAttributes.addFlashAttribute("successMessage", "Train journeys created successfully!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create train journey: " + e.getMessage());
//        }
//        return "redirect:/admin/trainjourneys/index";
//    }
}
