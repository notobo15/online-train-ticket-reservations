package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.TrainJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainJourneyRepository extends JpaRepository<TrainJourney, Integer> {

//    @Query("SELECT rr FROM RailwayRoute rr WHERE rr.isOpen = true AND rr.departureDate = :departureDate")
//    List<RailwayRoute> findOpenRoutesByDate(LocalDate departureDate);
    @Query("SELECT tj FROM TrainJourney tj " +
            "JOIN tj.train t " +
            "JOIN t.routes rStart " +
            "JOIN t.routes rEnd " +
            "WHERE tj.departureDate = :departureDate " +
            "AND (:isRoundTrip = false OR tj.arrivalDate = :arrivalDate) " +
            "AND rStart.startStation.stationId = :startStation " +
            "AND rEnd.endStation.stationId = :endStation " +
            "AND rStart.stationNumber < rEnd.stationNumber")
    List<TrainJourney> findTrainJourneys(@Param("departureDate") LocalDate departureDate,
                                         @Param("arrivalDate") LocalDate arrivalDate,
                                         @Param("startStation") Integer startStation,
                                         @Param("endStation") Integer endStation,
                                         @Param("isRoundTrip") boolean isRoundTrip);


}