package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByStartStationAndEndStation(Station startStation, Station endStation);

    @Query("SELECT r FROM Route r " +
            "JOIN r.train t " +
            "WHERE r.train.trainId = :trainId " +
            "AND r.stationNumber >= (SELECT rStart.stationNumber FROM Route rStart " +
            "WHERE rStart.startStation.code = :departureCode AND rStart.train.trainId = t.trainId " +
            "ORDER BY rStart.stationNumber ASC LIMIT 1) " + // Giới hạn về 1 hàng
            "AND r.stationNumber <= (SELECT rEnd.stationNumber FROM Route rEnd " +
            "WHERE rEnd.endStation.code = :arrivalCode AND rEnd.train.trainId = t.trainId " +
            "ORDER BY rEnd.stationNumber ASC LIMIT 1)")
        // Giới hạn về 1 hàng
    List<Route> findRoutesBetweenStations(@Param("departureCode") String departureCode,
                                          @Param("arrivalCode") String arrivalCode,
                                          @Param("trainId") Integer trainId);
    List<Route> findByTrain(Train train);
}