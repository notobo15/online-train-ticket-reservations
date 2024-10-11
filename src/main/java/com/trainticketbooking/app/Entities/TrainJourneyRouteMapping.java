package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "train_journey_route_mapping")
@Data
public class TrainJourneyRouteMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "train_journey_id", nullable = false)
    private TrainJourney trainJourney;

    @Column(name = "status")
    private String status;  // Active, Inactive, Suspended
}
