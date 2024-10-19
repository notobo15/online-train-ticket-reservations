package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
    public Page<Train> findAll(Pageable pageable);
}