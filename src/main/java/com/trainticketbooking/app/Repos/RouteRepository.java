package com.trainticketbooking.app.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Station;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByStartStationAndEndStation(Station startStation, Station endStation);
}