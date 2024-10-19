package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarriageSeatMappingRepository extends JpaRepository<CarriageSeatMapping, Integer> {
    List<CarriageSeatMapping> findByCarriageCarriageId(Integer carriageId);
}