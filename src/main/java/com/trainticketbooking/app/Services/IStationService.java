package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Entities.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStationService extends IService<Station> {
    public boolean existsByStationId(Integer stationId);
    Page<Station> findAll(Pageable pageable);
    public Station adminUpdateStation(Station station);

}
