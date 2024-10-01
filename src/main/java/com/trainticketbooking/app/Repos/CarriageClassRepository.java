package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarriageClassRepository extends JpaRepository<CarriageClass, Integer> {
}
