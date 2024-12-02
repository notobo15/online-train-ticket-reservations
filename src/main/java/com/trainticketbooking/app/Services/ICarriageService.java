package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICarriageService extends IService<Carriage>{
    Page<Carriage> findAll(Pageable pageable);
}
