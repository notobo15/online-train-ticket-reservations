package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

//    @ManyToOne
//    @JoinColumn(name = "passenger_id", nullable = false)
//    private Passenger passenger;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> tickets;

    @OneToOne(mappedBy = "booking")
    private Payment payment;
}