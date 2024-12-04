package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "seatholds")
@Data
public class SeatHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = true)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = true)
    private Train train;

    @ManyToOne
    @JoinColumn(name = "departure_station_id", nullable = true)
    private Station departureStation;

    @ManyToOne
    @JoinColumn(name = "arrival_station_id", nullable = true)
    private Station arrivalStation;

    @Column(name = "departure_date", nullable = true)
    private LocalDate departureDate;

    @Column(name = "hold_start_time", nullable = true)
    private LocalDateTime holdStartTime;

    @Column(name = "expiration_time", nullable = true)
    private LocalDateTime expirationTime;

    @Column(name = "status", nullable = true, length = 50)
    private String status;

    @Column(name = "is_departure", nullable = false)
    private boolean isDeparture;  // `true` for departure, `false` for return

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @PrePersist
    public void setExpirationTime() {
        this.holdStartTime = LocalDateTime.now();
        this.expirationTime = holdStartTime.plusMinutes(10);
    }
}