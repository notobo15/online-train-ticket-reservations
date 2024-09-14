package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainScheduleRepository trainScheduleRepository;

    @Autowired
    private TrainCarRepository trainCarRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private CompartmentRepository compartmentRepository;

    @Autowired
    private RouteRepository routeRepository;

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        // Lấy tất cả các ga tàu (stations) để hiển thị trong dropdown form
        List<Station> stations = stationRepository.findAll();
        model.addAttribute("stations", stations);
        return "booking/booking-form";
    }

    @GetMapping("/search-trains")
    public String searchTrains(@RequestParam("startStation") Integer startStationId,
                               @RequestParam("endStation") Integer endStationId,
                               Model model) {

        Station startStation = stationRepository.findById(startStationId).orElse(null);
        Station endStation = stationRepository.findById(endStationId).orElse(null);

        List<Route> routes = routeRepository.findByStartStationAndEndStation(startStation, endStation);

        model.addAttribute("routes", routes);
        return "booking/train-list";
    }

    @GetMapping("/train-details/{trainId}")
    public String showTrainDetails(@PathVariable("trainId") Long trainId, Model model) {
        Train train = trainRepository.findById(trainId).orElse(null);
        List<TrainCar> trainCars = trainCarRepository.findByTrain(train);
//        System.out.println(trainCars);
        model.addAttribute("trainCars", trainCars);
        return "booking/train-details";
    }

    @GetMapping("/compartment/{compartmentId}/seats")
    public String showSeats(@PathVariable("compartmentId") Long compartmentId, Model model) {
        // Lấy danh sách chỗ ngồi cho từng khoang
        Compartment compartment = compartmentRepository.findById(compartmentId).orElse(null);
        List<Seat> seats = seatRepository.findByCompartment(compartment);

        model.addAttribute("seats", seats);
        return "booking/seat-list";
    }
}