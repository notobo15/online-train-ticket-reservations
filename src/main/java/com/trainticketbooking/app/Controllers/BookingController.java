package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Services.RailwayNetworkService;
import com.trainticketbooking.app.Services.RailwayRouteService;
import com.trainticketbooking.app.Services.TrainService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class BookingController {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainScheduleRepository trainScheduleRepository;

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private CompartmentRepository compartmentRepository;

    @Autowired
    private TrainService trainService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RailwayNetworkService railwayNetworkService;

    @Autowired
    private RailwayRouteService railwayRouteService;

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        List<Station> stations = stationRepository.findAll();
        model.addAttribute("stations", stations);
        return "booking/booking-form";
    }

    @PostMapping("/search")
    public String searchTrains(
            @RequestParam("departureStationId") Integer departureStationId,
            @RequestParam("destinationStationId") Integer destinationStationId,
            @RequestParam("departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            Model model) {

        List<Train> availableTrains = railwayRouteService.findRailwayRouteWithStations(departureStationId,
                destinationStationId, departureDate);

        model.addAttribute("availableTrains", availableTrains);
        return "booking/train-results";
    }

    @GetMapping("/view-seats/{trainId}")
    public String viewSeats(@PathVariable("trainId") Integer trainId, Model model) {
        List<Seat> availableSeats = trainService.findSeatsByTrainId(trainId);
        List<Carriage> carriages = trainService.findCarriagesByTrainId(trainId);

        Map<Long, List<Integer>> floorNumbersMap = new HashMap<>();

        for (Carriage carriage : carriages) {
            List<Integer> floorNumbers = IntStream.rangeClosed(1, carriage.getTotalFloors())
                    .boxed()
                    .collect(Collectors.toList());
            floorNumbersMap.put(carriage.getCarriageId(), floorNumbers);
        }

        model.addAttribute("availableSeats", availableSeats);
        model.addAttribute("carriages", carriages);
        model.addAttribute("floorNumbersMap", floorNumbersMap);

        return "booking/seat-details";
    }

    @GetMapping("/train-details/{trainId}")
    public String showTrainDetails(@PathVariable("trainId") Long trainId, Model model) {
        Train train = trainRepository.findById(trainId).orElse(null);
        List<Carriage> trainCars = carriageRepository.findByTrain(train);
        model.addAttribute("trainCars", trainCars);
        return "booking/train-details";
    }
}