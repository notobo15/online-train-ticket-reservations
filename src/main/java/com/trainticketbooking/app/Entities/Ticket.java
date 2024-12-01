package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = true)
    private Passenger passenger;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "booking_datetime", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @ManyToOne
    @JoinColumn(name = "end_station_id")
    private Station endStation;

//    @ManyToOne
//    @JoinColumn(name = "carriage_seat_id", nullable = false)
//    private CarriageSeatMapping carriageSeatMapping;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @OneToOne()
    @JoinColumn(name = "return_ticket_id", referencedColumnName = "ticket_id", nullable = true)
    private Ticket returnTicket;

//    @ManyToOne
//    @JoinColumn(name = "ticket_type", nullable = false)
//    private TicketType ticketType;
}