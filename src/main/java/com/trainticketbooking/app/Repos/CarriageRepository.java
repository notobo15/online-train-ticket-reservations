package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.Carriage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CarriageRepository extends JpaRepository<Carriage, Long> {
    List<Carriage> findByTrain(Train train);

    List<Carriage> findByTrainTrainId(Integer trainId);
}
