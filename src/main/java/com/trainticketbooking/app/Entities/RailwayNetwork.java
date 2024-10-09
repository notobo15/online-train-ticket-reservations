package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "departure_station")
    @NotNull(message = "Departure Station cannot be empty")
    private Station departureStation;

    @ManyToOne
    @JoinColumn(name = "destination_station")
    @NotNull(message = "Destination Station cannot be empty")
    private Station destinationStation;

    @OneToMany(mappedBy = "railwayNetwork")
    private List<RailwayRoute> railwayRoutes;
}

