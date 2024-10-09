package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "railway_networks")
@Data
public class RailwayNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "railway_neworks_id")
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
    private List<Train> trains;

    @Column(name = "status")
    private String status; // Active, Maintenance, Suspended
}

