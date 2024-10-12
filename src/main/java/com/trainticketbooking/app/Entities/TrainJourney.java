package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "train_journeys")
@Data
public class TrainJourney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_journey_id")
    private Integer trainJourneyId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;


    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date", nullable = true)
    private LocalDate arrivalDate;

    @Column(name = "status")
    private String status; // Open, Closed, Delayed, Upcoming, Cancelled, In Progress
}
