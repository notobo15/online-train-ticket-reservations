package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface RailwayNetworkRepository extends JpaRepository<RailwayNetwork, Integer> {

    @Query(value = "SELECT rr.* FROM railway_routes rr " +
            "JOIN railway_networks rn ON rr.railway_network_id = rn.railway_id " +
            "JOIN stations ds ON rn.departure_station_id = ds.station_id " +
            "JOIN stations des ON rn.destination_station_id = des.station_id " +
            "WHERE ds.station_id = :departureStationId " +
            "AND des.station_id = :destinationStationId " +
            "AND rr.is_open = true " +
            "AND rr.departure_date = :departureDate", nativeQuery = true)
    List<RailwayNetwork> findOpenNetworksWithStations(Integer departureStationId, Integer destinationStationId, LocalDate departureDate);
}