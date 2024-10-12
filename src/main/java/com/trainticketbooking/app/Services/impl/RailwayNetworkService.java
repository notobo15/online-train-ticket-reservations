package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Repos.RailwayNetworkRepository;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return railwayNetworkRepository.findAll();
    }

    @Override
    public Optional<RailwayNetwork> getById(Integer id) {
        return railwayNetworkRepository.findById(id);
    }

    @Override
    public RailwayNetwork save(RailwayNetwork railwayNetwork) {
    public RailwayNetwork save(@Valid RailwayNetwork railwayNetwork) {
        // Save a new railway network or update an existing one
        return railwayNetworkRepository.save(railwayNetwork);
    }

    @Override
    public void deleteById(Integer id) {
        railwayNetworkRepository.deleteById(id);
    }

    @Override
    public RailwayNetwork update(RailwayNetwork railwayNetwork) {
        Optional<RailwayNetwork> existingNetwork = railwayNetworkRepository.findById(railwayNetwork.getRailwayId());
        if (existingNetwork.isPresent()) {
            RailwayNetwork updatedNetwork = existingNetwork.get();
            updatedNetwork.setName(railwayNetwork.getName());
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