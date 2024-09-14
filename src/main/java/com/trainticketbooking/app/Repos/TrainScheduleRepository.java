package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long> {
    List<TrainSchedule> findByRoute_StartStationAndRoute_EndStationAndDepartureTimeBetween(Station startStation, Station endStation, LocalDateTime startTime, LocalDateTime endTime);
}
