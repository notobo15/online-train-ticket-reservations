package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "method", nullable = false)
    private String method;  // CreditCard, PayPal

    @Column(name = "status", nullable = false)
    private String status;  // Paid, Pending, Failed
}
