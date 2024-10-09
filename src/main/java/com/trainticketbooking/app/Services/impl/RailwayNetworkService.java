package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Entities.RailwayRoute;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.RailwayNetworkRepository;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RailwayNetworkService implements IRailwayNetworkService {

    @Autowired
    private RailwayNetworkRepository railwayNetworkRepository;

    @Override
    public List<RailwayNetwork> getAll() {
        // Return all railway networks from the database
        return railwayNetworkRepository.findAll();
    }

    @Override
    public Optional<RailwayNetwork> getById(Integer id) {
        // Find a railway network by its ID
        return railwayNetworkRepository.findById(id);
    }

    @Override
    public RailwayNetwork save(RailwayNetwork railwayNetwork) {
        // Save a new railway network or update an existing one
        return railwayNetworkRepository.save(railwayNetwork);
    }

    @Override
    public void deleteById(Integer id) {
        // Delete a railway network by its ID
        railwayNetworkRepository.deleteById(id);
    }

    @Override
    public RailwayNetwork update(RailwayNetwork railwayNetwork) {
        // Update a railway network if it exists
        Optional<RailwayNetwork> existingNetwork = railwayNetworkRepository.findById(railwayNetwork.getRailwayId());
        if (existingNetwork.isPresent()) {
            RailwayNetwork updatedNetwork = existingNetwork.get();
            updatedNetwork.setName(railwayNetwork.getName());
            updatedNetwork.setDepartureStation(railwayNetwork.getDepartureStation());
            updatedNetwork.setDestinationStation(railwayNetwork.getDestinationStation());
            // Update any other fields if necessary
            return railwayNetworkRepository.save(updatedNetwork);
        } else {
            throw new RuntimeException("Railway network not found with ID: " + railwayNetwork.getRailwayId());
        }
    }

    @Override
    public Page<RailwayNetwork> findAll(Pageable pageable) {
        return railwayNetworkRepository.findAll(pageable);
    }
}