package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    List<Station> findAll();

    @Query("SELECT s FROM Station s WHERE LOWER(s.stationName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Station> searchStations(@Param("search") String search);
}
