package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.RouteRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.IRouteService;
import com.trainticketbooking.app.Services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService implements IRouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<Route> getAll() {
        return routeRepository.findAll();
    }

    @Override
    public Optional<Route> getById(Integer id) {
        return routeRepository.findById(id.longValue());
    }

    @Override
    public Route save(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteById(Integer id) {
        routeRepository.deleteById(id.longValue());
    }

    @Override
    public Route update(Route route) {
        Optional<Route> existingRoute = routeRepository.findById(route.getRouteId());
        if (existingRoute.isPresent()) {
            Route updatedRoute = existingRoute.get();
            updatedRoute.setStartStation(route.getStartStation());
            updatedRoute.setEndStation(route.getEndStation());
            updatedRoute.setDistance(route.getDistance());
            updatedRoute.setArrivalTime(route.getArrivalTime());
            updatedRoute.setDepartureTime(route.getDepartureTime());
            updatedRoute.setStationNumber(route.getStationNumber());
            updatedRoute.setDateNumber(route.getDateNumber());
            return routeRepository.save(updatedRoute);
        } else {
            throw new RuntimeException("Route not found with ID: " + route.getRouteId());
        }
    }
}