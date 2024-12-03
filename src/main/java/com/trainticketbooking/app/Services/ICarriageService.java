package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICarriageService extends IService<Carriage>{
    Page<Carriage> findAll(Pageable pageable);
    List<Carriage> findByTrain(Train train);
    public List<Carriage> findByTrainTrainId(Integer train);
}
