package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SeatHoldService {

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TrainJourneyRepository trainJourneyRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private StationRepository stationRepository;

    // Create or update a SeatHold
    public SeatHold createSeatHold(CreateSeatHoldRequestDto seatHoldRequestDto) {
        // 1. Find the train
        Optional<Train> trainOptional = trainRepository.findById(seatHoldRequestDto.getTrainId());
        if (trainOptional.isEmpty()) {
            throw new RuntimeException("Train not found with ID: " + seatHoldRequestDto.getTrainId());
        }

        Train train = trainOptional.get();

        // 2. Find the routes for the given train
        List<Route> routes = routeRepository.findByTrain(train);

        // 3. Find the journey for the specific date
        LocalDate journeyDate = seatHoldRequestDto.getDepartureDate();
        TrainJourney trainJourney = trainJourneyRepository.findByTrainAndDepartureDate(train, journeyDate);
        if (trainJourney == null) {
            throw new RuntimeException("No train journey found for the train ID: " + seatHoldRequestDto.getTrainId() +
                    " on the date: " + journeyDate);
        }

        // 4. Check for conflicts with the requested route and seat holds
        for (Route route : routes) {
            boolean isConflict = checkForConflict(route, seatHoldRequestDto);
            if (isConflict) {
                throw new RuntimeException("Conflict detected: Seat already held for this segment.");
            }
        }

        // 5. Create a new SeatHold entry if no conflict
        SeatHold seatHold = new SeatHold();
        seatHold.setTrain(train);
        seatHold.setDepartureDate(seatHoldRequestDto.getDepartureDate());
        seatHold.setStatus("HOLD");

        // Fetch the stations from the repository
        Optional<Station> departureStationOptional = stationRepository.findById(seatHoldRequestDto.getDepartureStationId());
        Optional<Station> arrivalStationOptional = stationRepository.findById(seatHoldRequestDto.getArrivalStationId());

        if (departureStationOptional.isEmpty() || arrivalStationOptional.isEmpty()) {
            throw new RuntimeException("Invalid station IDs provided.");
        }

        seatHold.setDepartureStation(departureStationOptional.get());
        seatHold.setArrivalStation(arrivalStationOptional.get());

        return seatHoldRepository.save(seatHold); // Save the seat hold in the DB
    }

    /**
     * Checks if the requested seat hold conflicts with any existing seat holds
     *
     * @param route              The route to check against
     * @param seatHoldRequestDto The seat hold request DTO containing the booking info
     * @return True if there's a conflict, false otherwise
     */
    private boolean checkForConflict(Route route, CreateSeatHoldRequestDto seatHoldRequestDto) {
        // Fetch existing seat holds for this train and date
        List<SeatHold> existingSeatHolds = seatHoldRepository.findByTrainAndDepartureDate(route.getTrain(), seatHoldRequestDto.getDepartureDate());

        // Check if the requested booking overlaps with any existing seat hold in the same route
        for (SeatHold existingSeatHold : existingSeatHolds) {
            // Check if the departure or arrival station overlap with any existing seat hold
            boolean isConflict = isOverlapping(existingSeatHold, seatHoldRequestDto, route);
            if (isConflict) {
                return true;  // Conflict found
            }
        }

        return false;  // No conflict
    }

    /**
     * Check if two seat hold requests overlap based on station segments
     *
     * @param existingSeatHold   The existing seat hold to check against
     * @param seatHoldRequestDto The new seat hold request
     * @param route              The route to check against
     * @return True if there is an overlap
     */
    private boolean isOverlapping(SeatHold existingSeatHold, CreateSeatHoldRequestDto seatHoldRequestDto, Route route) {
        // Get the departure and arrival station for the existing seat hold
        Station existingDeparture = existingSeatHold.getDepartureStation();
        Station existingArrival = existingSeatHold.getArrivalStation();

        // Check if the requested departure station overlaps with any existing arrival station
        // and vice versa (this can happen when departure is part of an existing seat's journey)
        boolean isDepartureOverlapping = seatHoldRequestDto.getDepartureStationId() >= existingDeparture.getStationId() &&
                seatHoldRequestDto.getDepartureStationId() <= existingArrival.getStationId();

        boolean isArrivalOverlapping = seatHoldRequestDto.getArrivalStationId() >= existingDeparture.getStationId() &&
                seatHoldRequestDto.getArrivalStationId() <= existingArrival.getStationId();

        // Ensure that requested route is within the same route segment (based on the route stations' order)
//        boolean isWithinRoute = isStationInRoute(seatHoldRequestDto.getDepartureStationId(), seatHoldRequestDto.getArrivalStationId(), route);

//        return (isDepartureOverlapping || isArrivalOverlapping) && isWithinRoute;
        return true;
    }


    /**
     * Deletes a SeatHold by its ID.
     *
     * @param id The ID of the SeatHold to delete
     */
    public void deleteSeatHoldById(Integer id) {
        // Check if SeatHold exists before deleting
        if (seatHoldRepository.existsById(id)) {
            seatHoldRepository.deleteById(id);
        } else {
            throw new RuntimeException("SeatHold not found with ID: " + id);
        }
    }
}
