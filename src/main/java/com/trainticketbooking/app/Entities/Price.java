package com.trainticketbooking.app.Entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_type_id",referencedColumnName = "seat_type_id")
    private SeatType seatType;


    @Column(name = "surcharge_percentage", nullable = true)
    private Double surchargePercentage;

    @Column(name = "price_per_km", nullable = false)
    @NotNull(message = "PricePerKm is required!")
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
