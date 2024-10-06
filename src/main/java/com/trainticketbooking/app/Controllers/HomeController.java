package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Dtos.BookingFormDto;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.IStationService;
import com.trainticketbooking.app.Services.impl.RailwayNetworkService;
import com.trainticketbooking.app.Services.impl.RailwayRouteService;
import com.trainticketbooking.app.Services.impl.TrainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    public IStationService stationService;

    @Autowired
    private RailwayNetworkService railwayNetworkService;

    @Autowired
    private RailwayRouteService railwayRouteService;

    @Autowired
    private TrainService trainService;

    @GetMapping(value = {"", "/tim-ve", "/dat-ve"})
    public String homePage(Model model) {
        model.addAttribute("trainBookingForm", new BookingFormDto());
        model.addAttribute("stations", stationService.getAll());
        return "home/index";
    }

    @PostMapping(value = {"/search/results"})
    public String searchPage( @Valid @ModelAttribute("trainBookingForm") BookingFormDto form,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("stations", stationService.getAll()); // Include stations in case of form re-display
            return "home/index";
        }
        List<Train> availableTrains = railwayRouteService.findRailwayRouteWithStations(form.getDepartureStationId(),
                form.getDestinationStationId(), form.getDepartureDate());
        model.addAttribute("availableTrains", availableTrains);
        return "home/search";
    }

    @GetMapping("/trains/{trainId}/seats")
    public String viewSeats(@PathVariable("trainId") Integer trainId, Model model) {
        List<Seat> availableSeats = trainService.findSeatsByTrainId(trainId);
        List<Carriage> carriages = trainService.findCarriagesByTrainId(trainId);

        Map<Integer, List<Integer>> floorNumbersMap = new HashMap<>();

        for (Carriage carriage : carriages) {
            List<Integer> floorNumbers = IntStream.rangeClosed(1, carriage.getTotalFloors())
                    .boxed()
                    .collect(Collectors.toList());
            floorNumbersMap.put(carriage.getCarriageId(), floorNumbers);
        }

        model.addAttribute("availableSeats", availableSeats);
        model.addAttribute("carriages", carriages);
        model.addAttribute("floorNumbersMap", floorNumbersMap);

        return "home/seat-details";
    }
}
