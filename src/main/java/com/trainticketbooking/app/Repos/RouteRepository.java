package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByStartStationAndEndStation(Station startStation, Station endStation);
}