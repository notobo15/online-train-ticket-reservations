package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Province.ProvinceDTO;
import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Mappers.ProvinceMapper;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.StationRepository;
import com.trainticketbooking.app.Services.ISeatService;
import com.trainticketbooking.app.Services.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService implements IStationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<Station> getAll() {
        return stationRepository.findAll();
    }

    @Override
    public Optional<Station> getById(Integer id) {
        // Find a station by its ID
        return stationRepository.findById(id);
    }

    @Override
    public Station save(Station station) {
        return stationRepository.save(station);
    }

    @Override
    public void deleteById(Integer id) {
        stationRepository.deleteById(id);
    }

    @Override
    public Station update(Station station) {
        Optional<Station> existingStation = stationRepository.findById(station.getStationId());
        if (existingStation.isPresent()) {
            Station updatedStation = existingStation.get();
            updatedStation.setStationName(station.getStationName());
            updatedStation.setProvince(station.getProvince());
            return stationRepository.save(updatedStation);
        } else {
            throw new RuntimeException("Station not found with ID: " + station.getStationId());
        }
    }

    @Override
    public boolean existsByStationId(Integer stationId) {
        return stationRepository.existsByStationId(stationId);
    }

    @Override
    public Page<Station> findAll(Pageable pageable) {
        return stationRepository.findAll(pageable);
    }

    @Override
    public Station adminUpdateStation(Station station) {
        Station stationTemp = getById(station.getStationId()).get();
        return stationRepository.save(station);
    }
}