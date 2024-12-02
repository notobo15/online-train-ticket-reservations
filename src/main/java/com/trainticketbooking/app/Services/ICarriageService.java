package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICarriageService extends IService<Carriage>{
    Page<Carriage> findAll(Pageable pageable);
    Integer getTicketNumberOfCarriage(Integer carriageId);
    List<Ticket> getTicketsOfCarriage(Integer carriageId);
    List<Seat> getSeatsOfCarriage(Integer carriageId);
    CarriageSeatMapping getCSMByCarriageIdAndSeatId(Integer carriageId, Integer seatId);
}
