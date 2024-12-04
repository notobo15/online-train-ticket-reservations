package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Price;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Repos.PriceRepository;
import com.trainticketbooking.app.Repos.SeatTypeRepository;
import com.trainticketbooking.app.Services.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService implements IPriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Override
    public List<Price> getAll() {
        return priceRepository.findAll();
    }

    @Override
    public Optional<Price> getById(Integer id) {
        return priceRepository.findById(id);
    }

    @Override
    public Price save(Price price) {
        price.setSeatType(null);
        return priceRepository.save(price);
    }

    @Override
    public void deleteById(Integer id) {
        Price price = priceRepository.findById(id).orElseThrow();

        if (price.getSeatType() != null) {
            SeatType seatType = price.getSeatType();
            price.setSeatType(null);
            seatTypeRepository.save(seatType);
        }

        priceRepository.delete(price);
    }

    @Override
    public Price update(Price price) {
        Optional<Price> existingPrice = priceRepository.findById(price.getPriceId());
        if (existingPrice.isPresent()) {
            Price updatedPrice = existingPrice.get();
            updatedPrice.setSurchargePercentage(price.getSurchargePercentage());
            updatedPrice.setPricePerKm(price.getPricePerKm());
            // Add any other fields you need to update
            return priceRepository.save(updatedPrice);
        } else {
            throw new RuntimeException("Price not found with ID: " + price.getPriceId());
        }
    }

    public List<Price> findAvailablePrices() {
        return priceRepository.findBySeatTypeIsNull();
    }

    @Override
    public Page<Price> findAll(Pageable pageable) {
        return priceRepository.findAll(pageable);
    }
}