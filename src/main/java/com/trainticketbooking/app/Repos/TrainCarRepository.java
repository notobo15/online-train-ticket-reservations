package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.TrainCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TrainCarRepository extends JpaRepository<TrainCar, Long> {
    List<TrainCar> findByTrain(Train train);
}
