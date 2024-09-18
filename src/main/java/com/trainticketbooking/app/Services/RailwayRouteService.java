package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.RailwayRoute;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.RailwayRouteRepository;
import com.trainticketbooking.app.Repos.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RailwayRouteService {

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
}