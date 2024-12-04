package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPriceService extends IService<Price> {

    List<Price> findAvailablePrices();
    Page<Price> findAll(Pageable pageable);
}