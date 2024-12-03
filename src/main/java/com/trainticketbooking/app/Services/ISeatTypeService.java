package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.SeatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISeatTypeService extends IService<SeatType> {
    Page<SeatType> findAll(Pageable pageable);
}
