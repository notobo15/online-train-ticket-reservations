package com.trainticketbooking.app.Entities;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "trains")
@Data
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_id")
    private Integer trainId;

    @Column(name = "train_number", unique = true, length = 20, nullable = false)
    private String trainNumber;

    @Column(name = "train_type", length = 50)
    private String trainType;

    @OneToMany(mappedBy = "train")
    private List<TrainJourney> trainJourneys;

    @OneToMany(mappedBy = "train")
    private List<Carriage> carriages;

    @OneToMany(mappedBy = "train")
    private List<Route> routes;

    @ManyToOne
    @JoinColumn(name = "railway_network_id", nullable = false)
    private RailwayNetwork railwayNetwork;
}