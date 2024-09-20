package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Entities.RailwayRoute;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Repos.RailwayNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RailwayNetworkService {

    @Autowired
    private RailwayNetworkRepository railwayNetworkRepository;

}