package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.TrainJourney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainJourneyRepository extends JpaRepository<TrainJourney, Integer> {

//    @Query("SELECT rr FROM RailwayRoute rr WHERE rr.isOpen = true AND rr.departureDate = :departureDate")
//    List<RailwayRoute> findOpenRoutesByDate(LocalDate departureDate);
}