package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.TrainJourney;
import com.trainticketbooking.app.Repos.RailwayNetworkRepository;
import com.trainticketbooking.app.Repos.RouteRepository;
import com.trainticketbooking.app.Repos.TrainJourneyRepository;
import com.trainticketbooking.app.Services.ITrainJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainJourneyService implements ITrainJourneyService {

    @Autowired
    private TrainJourneyRepository trainJourneyRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RailwayNetworkRepository railwayNetworkRepository;

    public List<Train> findTrainsWithStations(Integer departureStationId, Integer destinationStationId, LocalDate departureDate) {

        // Lấy danh sách tất cả các RailwayNetwork (chuyến đi lớn)
        List<RailwayNetwork> allNetworks = railwayNetworkRepository.findAll();
        List<Train> availableTrains = new ArrayList<>();

//        // Duyệt qua tất cả các RailwayNetwork
//        for (RailwayNetwork railwayNetwork : allNetworks) {
//            List<Route> routes = railwayNetwork.getRoutes(); // Các chặng trong chuyến đi
//
//            boolean hasDepartureStation = false;
//            boolean hasDestinationStation = false;
//
//            // Duyệt qua các Route trong RailwayNetwork hiện tại để tìm ga đi và ga đến
//            for (Route route : routes) {
//                if (route.getStartStation().getStationId().equals(departureStationId)) {
//                    hasDepartureStation = true;
//                }
//                if (route.getEndStation().getStationId().equals(destinationStationId)) {
//                    hasDestinationStation = true;
//                }
//
//                // Nếu cả ga đi và ga đến đều có trong chuyến đi
//                if (hasDepartureStation && hasDestinationStation) {
//                    // Tìm RailwayRoute khớp với ngày khởi hành
//                    railwayNetwork.getTrainJourneys().stream()
//                            .filter(railwayRoute -> railwayRoute.getDepartureDate().equals(departureDate)) // So sánh trực tiếp với LocalDate
//                            .findFirst() // Lấy tuyến đầu tiên khớp với điều kiện
//                            .ifPresent(railwayRoute -> availableTrains.add(railwayRoute.getTrain())); // Thêm tàu vào danh sách
//
//                    break; // Đã tìm thấy chuyến đi phù hợp, không cần kiểm tra thêm
//                }
//            }
//        }
        return availableTrains;
    }
    @Override
    public List<TrainJourney> getAll() {
        return trainJourneyRepository.findAll();
    }

    @Override
    public Optional<TrainJourney> getById(Integer id) {
        return trainJourneyRepository.findById(id);
    }

    @Override
    public TrainJourney save(TrainJourney trainJourney) {
        return trainJourneyRepository.save(trainJourney);
    }

    @Override
    public void deleteById(Integer id) {
        trainJourneyRepository.deleteById(id);
    }

    @Override
    public TrainJourney update(TrainJourney trainJourney) {
        Optional<TrainJourney> existingJourney = trainJourneyRepository.findById(trainJourney.getTrainJourneyId());
        if (existingJourney.isPresent()) {
            TrainJourney updatedJourney = existingJourney.get();
            updatedJourney.setTrain(trainJourney.getTrain());
            updatedJourney.setRailwayNetwork(trainJourney.getRailwayNetwork());
            updatedJourney.setDepartureDate(trainJourney.getDepartureDate());
            updatedJourney.setArrivalDate(trainJourney.getArrivalDate());
            updatedJourney.setStatus(trainJourney.getStatus());
            return trainJourneyRepository.save(updatedJourney);
        } else {
            throw new RuntimeException("Train Journey not found with ID: " + trainJourney.getTrainJourneyId());
        }
    }
}