package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "compartment")
@Data
public class Compartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compartment_id")
    private Long compartmentId;

    @ManyToOne
    @JoinColumn(name = "carriage_id", nullable = false)
    private Carriage carriage;

    @OneToMany(mappedBy = "compartment")
    private List<Seat> seats;

    @Column(name = "compartment_number", nullable = false)
    private String compartmentNumber;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "floor")
    private int floor;

}