package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Dtos.BookingFormDto;
import com.trainticketbooking.app.Dtos.CarriageDto;
import com.trainticketbooking.app.Dtos.SeatDto;
import com.trainticketbooking.app.Dtos.TrainDto;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.impl.CarriageService;
import com.trainticketbooking.app.Services.impl.RailwayRouteService;
import com.trainticketbooking.app.Services.impl.StationService;
import com.trainticketbooking.app.Services.impl.TrainService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookingApiController {

    @Autowired
    private TrainService trainService;

    @Autowired
    private RailwayRouteService railwayRouteService;

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StationService stationService;


    @GetMapping("/trains/{trainId}/carriages/{carriageId}/available-seats")
    public ResponseEntity<Map<String, Object>> getAvailableSeatsByCarriage(
            @PathVariable("trainId") Integer trainId,
            @PathVariable("carriageId") Long carriageId) {

        List<Seat> availableSeats = trainService.findSeatsByTrainId(trainId);

        List<Seat> emptySeats = availableSeats.stream()
                .filter(seat -> seat.getCarriage().getCarriageId().equals(carriageId)) // Lọc theo toa
//                .filter(seat -> seat.isStatus())  // Giả sử 'status' = true nghĩa là ghế còn trống
                .collect(Collectors.toList());

        List<SeatDto> seatDtos = emptySeats.stream()
                .map(seat -> modelMapper.map(seat, SeatDto.class))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("trainId", trainId);
        response.put("carriageId", carriageId);
        response.put("availableSeatCount", seatDtos.size());
        response.put("availableSeats", seatDtos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/trains/{trainId}/carriages")
    public ResponseEntity<List<CarriageDto>> getCarriages(@PathVariable Integer trainId) {
        List<Carriage> carriages = trainService.findCarriagesByTrainId(trainId);
        List<CarriageDto> carriageDTOs = carriages.stream()
                .map(carriage -> new CarriageDto(carriage.getCarriageId(), carriage.getCarNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(carriageDTOs);
    }

    @GetMapping("/carriages/{carriageId}/seats")
    public ResponseEntity<Map<String, Object>> getSeats(@PathVariable Integer carriageId) {
        List<Seat> seats = carriageService.findSeatsByCarriageId(carriageId);

        Optional<Carriage> carriageOpt = carriageService.getById(carriageId);
        if (!carriageOpt.isPresent()) {
        }
        var test = carriageOpt.get().getCarriageClass();
        Carriage carriage = carriageOpt.get();

        List<SeatDto> seatDTOs = seats.stream()
                .map(seat -> modelMapper.map(seat, SeatDto.class))
                .collect(Collectors.toList());

        CarriageDto carriageDto = modelMapper.map(carriage, CarriageDto.class);

        Map<String, Object> response = new HashMap<>();
        response.put("seats", seatDTOs);
        response.put("carriage", carriageDto);
        response.put("carriageType", carriageDto.getCarriageClass());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/search/results")
    public ResponseEntity<?> searchTrains(@Valid @RequestBody BookingFormDto form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        List<Train> availableTrains = railwayRouteService.findRailwayRouteWithStations(
                form.getDepartureStationId(),
                form.getDestinationStationId(),
                form.getDepartureDate());

        if (availableTrains.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No trains found for the given criteria.");
        }

        List<TrainDto> trainDTOs = availableTrains.stream()
                .map(train -> modelMapper.map(train, TrainDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(trainDTOs);
    }

    @GetMapping("/stations/list")
    public List<Station> getStations(@RequestParam(required = false) String search) {
        List<Station> stationList;
        if (search != null && !search.isEmpty()) {
            stationList = stationService.searchStations(search);
        }
        stationList = stationService.getAll();
        return stationList;
    }
}