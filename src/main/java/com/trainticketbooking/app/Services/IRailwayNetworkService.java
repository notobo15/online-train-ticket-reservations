package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Entities.RailwayRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRailwayNetworkService extends IService<RailwayNetwork> {
    public Page<RailwayNetwork> findAll(Pageable pageable);
}
