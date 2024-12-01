package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarriageSeatMappingRepository extends JpaRepository<CarriageSeatMapping, Integer> {
    List<CarriageSeatMapping> findByCarriageCarriageId(Integer carriageId);

    @Query("SELECT csm FROM CarriageSeatMapping csm " +
            "JOIN FETCH csm.seat s " +
            "JOIN FETCH s.seatType " +
            "WHERE csm.carriage.carriageId = :carriageId")
    List<CarriageSeatMapping> findSeatsByCarriageId(@Param("carriageId") Integer carriageId);

    int countByCarriageCarriageId(Integer carriageId);

}
