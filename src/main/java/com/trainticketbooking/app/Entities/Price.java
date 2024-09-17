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

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "status", nullable = false)
    private String status;

}
