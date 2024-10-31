package com.trainticketbooking.app.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "prices")
@Data
public class Price {

    private static final double MINIMUM_PRICE = 40000.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priceId")
    private Integer priceId;

    @OneToOne
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;


    @Column(name = "surcharge_percentage", nullable = true)
    private Double surchargePercentage;

    @Column(name = "price_per_km", nullable = false)
    private Double pricePerKm;

    public Double calTotalPrice(Double distanceInKm) {
        Double basePrice = pricePerKm * distanceInKm;

        // áp dụng phụ thu
        if (surchargePercentage != null && surchargePercentage > 0) {
            basePrice += basePrice * (surchargePercentage / 100);
        }

        if (basePrice < MINIMUM_PRICE) {
            basePrice = MINIMUM_PRICE;
        }

        return basePrice;
    }
}
