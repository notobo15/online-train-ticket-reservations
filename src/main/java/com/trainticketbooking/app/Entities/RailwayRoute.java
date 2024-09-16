package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "railway_routes")
@Data
public class RailwayRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "railway_route_id")
    private Integer railwayRouteId;


    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne
    @JoinColumn(name = "railway_network")
    private RailwayNetwork railwayNetwork;

    @Column(name = "departureDate")
    private LocalDate departureDate;

    @Column(name = "is_open")
    private boolean isOpen;


    @OneToMany(mappedBy = "railwayRoute")
    private List<Route> routes;
}
