package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICarriageService extends IService<Carriage>{
    Page<Carriage> findAll(Pageable pageable);
    List<CarriageSeatMapping> getCSMsByCarriageId(Integer carriageId);
    Integer getTicketNumberOfCarriage(Integer carriageId);
    List<Ticket> getTicketsOfCarriage(Integer carriageId);
    List<Seat> getSeatsOfCarriage(List<CarriageSeatMapping> csmMappings);
    CarriageSeatMapping getCSMByCarriageIdAndSeatId(Integer carriageId, Integer seatId);
}
