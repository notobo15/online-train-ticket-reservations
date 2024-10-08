package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.RailwayRoute;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.RailwayRouteRepository;
import com.trainticketbooking.app.Repos.RouteRepository;
import com.trainticketbooking.app.Services.IRailwayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RailwayRouteService implements IRailwayRouteService {

    @Autowired
    private RailwayRouteRepository railwayRouteRepository;

    @Autowired
    private RouteRepository routeRepository;

    public List<Train> findRailwayRouteWithStations(Integer departureStationId, Integer destinationStationId,
            LocalDate departureDate) {

        List<RailwayRoute> openRoutes = railwayRouteRepository.findOpenRoutesByDate(departureDate);
        List<Train> isAvailableTrains = new ArrayList<>();

        for (RailwayRoute railwayRoute : openRoutes) {
            List<Route> routes = railwayRoute.getRoutes();

            boolean hasDepartureStation = false;
            boolean hasDestinationStation = false;

            for (Route route : routes) {
                if (route.getStartStation().getStationId().equals(departureStationId)) {
                    hasDepartureStation = true;
                }
                if (route.getEndStation().getStationId().equals(destinationStationId)) {
                    hasDestinationStation = true;
                }
                if (hasDepartureStation && hasDestinationStation) {
                    isAvailableTrains.add(railwayRoute.getTrain());
                }
            }
        }
        return isAvailableTrains;
    }

    @Override
    public List<RailwayRoute> getAll() {
        return railwayRouteRepository.findAll();
    }

    @Override
    public Optional<RailwayRoute> getById(Integer id) {
        return railwayRouteRepository.findById(id);
    }

    @Override
    public RailwayRoute save(RailwayRoute railwayRoute) {
        return railwayRouteRepository.save(railwayRoute);
    }

    @Override
    public void deleteById(Integer id) {
        railwayRouteRepository.deleteById(id);
    }

    @Override
    public RailwayRoute update(RailwayRoute railwayRoute) {
        Optional<RailwayRoute> existingRoute = railwayRouteRepository.findById(railwayRoute.getRailwayRouteId());
        if (existingRoute.isPresent()) {
            RailwayRoute updatedRoute = existingRoute.get();
            updatedRoute.setTrain(railwayRoute.getTrain());
            updatedRoute.setRailwayNetwork(railwayRoute.getRailwayNetwork());
            updatedRoute.setDepartureDate(railwayRoute.getDepartureDate());
            updatedRoute.setOpen(railwayRoute.isOpen());
            updatedRoute.setRoutes(railwayRoute.getRoutes());
            return railwayRouteRepository.save(updatedRoute);
        } else {
            throw new RuntimeException("Railway Route not found with ID: " + railwayRoute.getRailwayRouteId());
        }
    }
}