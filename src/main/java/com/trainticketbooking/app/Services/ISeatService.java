package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISeatService extends IService<Seat> {
    Page<Seat> findAll(Pageable pageable);
}
