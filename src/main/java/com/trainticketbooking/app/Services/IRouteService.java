package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRouteService extends IService<Route> {
    Page<Route> findAll(Pageable pageable);
}
