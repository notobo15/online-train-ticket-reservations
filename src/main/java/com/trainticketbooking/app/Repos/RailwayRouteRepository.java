package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.RailwayRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface RailwayRouteRepository extends JpaRepository<RailwayRoute, Integer> {

    @Query("SELECT rr FROM RailwayRoute rr WHERE rr.isOpen = true AND rr.departureDate = :departureDate")
    List<RailwayRoute> findOpenRoutesByDate(LocalDate departureDate);
}