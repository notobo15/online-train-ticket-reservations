package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seat_route")
@Data
public class SeatRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_route_id")
    private Integer seatRouteId;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "price", nullable = false)
    private Double price;
}
