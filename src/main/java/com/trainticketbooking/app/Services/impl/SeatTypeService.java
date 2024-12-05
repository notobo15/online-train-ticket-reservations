package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Price;
import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Repos.PriceRepository;
import com.trainticketbooking.app.Repos.SeatTypeRepository;
import com.trainticketbooking.app.Services.ISeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SeatTypeService implements ISeatTypeService {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public List<SeatType> getAll() {
        return seatTypeRepository.findAll();
    }

    @Override
    public Optional<SeatType> getById(Integer id) {
        return seatTypeRepository.findById(Long.valueOf(id));
    }

    @Override
    public SeatType save(SeatType seatType) {
        return seatTypeRepository.save(seatType);
    }

    @Override
    public void deleteById(Integer id) {
        seatTypeRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public SeatType update(SeatType seatType) {
        Optional<SeatType> existingSeatType = seatTypeRepository.findById(seatType.getSeatTypeId());
        if (existingSeatType.isPresent()) {
            SeatType updatedSeatType = existingSeatType.get();
            updatedSeatType.setSeatType(seatType.getSeatType());
            updatedSeatType.setCode(seatType.getCode());
            updatedSeatType.setDescription(seatType.getDescription());
            return seatTypeRepository.save(updatedSeatType);
        } else {
            throw new RuntimeException("SeatType not found with ID: " + seatType.getSeatTypeId());
        }
    }

    @Override
    public Page<SeatType> findAll(Pageable pageable) {
        return seatTypeRepository.findAll(pageable);
    }

    @Override
    public SeatType saveWithPrice(SeatType seatType) {
        Optional<Price> existingPriceOpt = priceRepository
                .findById(seatType.getPrice().getPriceId());
        if (existingPriceOpt.isPresent()) {
            Price existingPrice = existingPriceOpt.get();
            seatType.setPrice(existingPrice);
            seatType = seatTypeRepository.save(seatType);
            existingPrice.setSeatType(seatType);
            priceRepository.save(existingPrice);
        }
        else {
            seatType.setPrice(null);
            seatType = seatTypeRepository.save(seatType);
        }
        return seatType;
    }

    @Override
    public SeatType updateWithPrice(SeatType seatType) {
        SeatType existingSeatType = seatTypeRepository
                .findById(seatType.getSeatTypeId())
                .orElseThrow(
                        ()->new RuntimeException("Seat type not found with id: " + seatType.getSeatTypeId()));
        existingSeatType.setCode(seatType.getCode());
        existingSeatType.setDescription(seatType.getDescription());
        existingSeatType.setSeatType(seatType.getSeatType());
        Price oldPrice = existingSeatType.getPrice();
        if (oldPrice != null && !Objects.equals(oldPrice.getPriceId(), seatType.getPrice().getPriceId())) {
            oldPrice.setSeatType(null);
            priceRepository.save(oldPrice);
        }
        Optional<Price> newPriceOpt = priceRepository
                .findById(seatType.getPrice().getPriceId());
        if (newPriceOpt.isPresent()) {
            Price newPrice = newPriceOpt.get();
            existingSeatType.setPrice(newPrice);
            newPrice.setSeatType(existingSeatType);
            priceRepository.save(newPrice);
        }
        existingSeatType.setPrice(null);
        return seatTypeRepository.save(existingSeatType);
    }

    @Override
    public void deleteByIdWithPrice(Integer id) {
        SeatType seatType  = seatTypeRepository.findById(Integer.toUnsignedLong(id)).orElseThrow();
        if (seatType.getPrice() != null) {
            Price price = seatType.getPrice();
            price.setSeatType(null);
            priceRepository.save(price);
        }
        seatTypeRepository.delete(seatType);
    }

}
