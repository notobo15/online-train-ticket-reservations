package com.trainticketbooking.app.Services;

import org.springframework.data.domain.Page;

import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Pageable;

public interface ITrainService extends IService<Train> {
    Page<Train> findAll(Pageable pageable);
}
