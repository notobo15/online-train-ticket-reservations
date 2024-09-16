package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "railway_networks")
@Data
public class RailwayNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "railway_id")
    private Integer railwayId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "departure_station")
    private Station departureStation;

    @ManyToOne
    @JoinColumn(name = "destination_station")
    private Station destinationStation;

    @OneToMany(mappedBy = "railwayNetwork")
    private List<RailwayRoute> railwayRoutes;
}

