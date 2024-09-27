package com.trainticketbooking.app.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trainticketbooking.app.Entities.Train;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
}