package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Services.IProvinceService;
import com.trainticketbooking.app.Services.IStationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/stations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StationController {
    IStationService stationService;
    IProvinceService provinceService;

    @GetMapping({"", "/index"})
    public String getAllStations(Model model, Pageable pageable) {

        Page<Station> stationsPage = stationService.findAll(pageable);

        model.addAttribute("stations", stationsPage.getContent());
        model.addAttribute("currentPage", stationsPage.getNumber());
        model.addAttribute("totalPages", stationsPage.getTotalPages());
        model.addAttribute("size", stationsPage.getSize());

        return "admin/stations/index";
    }

    @GetMapping("/create")
    public String createStation(Model model) {
        model.addAttribute("provinces", provinceService.getAll());
        model.addAttribute("station", new Station());
        return "admin/stations/create";
    }

    @PostMapping("/create")
    public String saveCreateStation(Model model, @ModelAttribute Station station) {
        try {
            Station stationResponse = stationService.save(station);
            model.addAttribute("station", new Station());
            model.addAttribute(
                    "successMessage",
                    "Station created successfully!");
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Station created fail!  " + e.getMessage());
        }
        return "admin/stations/create";
    }

    @GetMapping("/edit/{id}")
    public String editStation(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit station");
        try {
            Optional<Station> stationOptional = stationService.getById(id);
            Station station = stationOptional.get();
            model.addAttribute("provinces", provinceService.getAll());
            model.addAttribute("station", station);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Station edited fail!  " + e.getMessage());
        }
        return "admin/stations/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditStation(@PathVariable("id") Integer id,
                                  @ModelAttribute Station station,
                                  Model model) {
        log.info("Start save edit station");
        station.setStationId(id);

        try {
            log.info("Station update: {}", station.toString());
            stationService.adminUpdateStation(station);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited station with id = %d successfully!", station.getStationId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Station edited fail! " + e.getMessage());
        }
        model.addAttribute("provinces", provinceService.getAll());
        return "admin/stations/edit";
    }

    @GetMapping("detail/{id}")
    public String viewDetailStation(@PathVariable("id") Integer id, Model model) {
        log.info("Start detail station");
        try {
            Optional<Station> stationOptional = stationService.getById(id);
            Station station = stationOptional.get();
            model.addAttribute("provinces", provinceService.getAll()); // Including provinces for reference
            model.addAttribute("station", station);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "View detail station fail! " + e.getMessage());
        }
        return "admin/stations/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteStation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            stationService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete station success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete station fail! " + e.getMessage());
        }
        return "redirect:/admin/stations/index";
    }

}
