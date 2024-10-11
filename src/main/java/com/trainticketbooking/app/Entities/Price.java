package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prices")
@Data
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priceId")
    private Integer priceId;

//    @ManyToOne
//    @JoinColumn(name = "route_id")
//    private Route route;

//    @ManyToOne
//    @JoinColumn(name = "seat_type_id", nullable = false)
//    private SeatType seatType;

    @OneToOne
    @JoinColumn(name = "carriage_seat_id", nullable = false)
    private CarriageSeatMapping carriageSeat;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "price_per_km", nullable = false)
    private Double pricePerKm;

    public Double calculateTotalPrice(Double distanceInKm) {
        return pricePerKm * distanceInKm;
    }
}
