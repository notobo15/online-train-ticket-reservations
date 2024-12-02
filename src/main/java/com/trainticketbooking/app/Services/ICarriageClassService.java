package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICarriageClassService extends IService<CarriageClass>{
    Page<CarriageClass> findAll(Pageable pageable);
}
