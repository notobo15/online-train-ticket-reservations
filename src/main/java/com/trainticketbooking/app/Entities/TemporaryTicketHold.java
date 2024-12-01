package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "temporary_ticket_holds")
@Data
public class TemporaryTicketHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hold_id")
    private Integer holdId;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = true)
    private Seat seat;

    @Column(name = "train_id", nullable = true)
    private Integer trainId;

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

    @Column(name = "status", nullable = true)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

//    @Column(name = "is_round_trip", nullable = false)
//    private boolean isRoundTrip; // `true` cho vé khứ hồi, `false` cho vé một chiều

//    @OneToOne
//    @JoinColumn(name = "return_hold_id", referencedColumnName = "hold_id", nullable = true)
//    private TemporaryTicketHold returnHold;

    @Column(name = "is_departure", nullable = false)
    private boolean isDeparture; // `true` cho chiều đi, `false` cho chiều về

    @PrePersist
    public void setExpirationTime() {
        this.holdStartTime = LocalDateTime.now();
        this.expirationTime = holdStartTime.plusMinutes(10);
    }
}
