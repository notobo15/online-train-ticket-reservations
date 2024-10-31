package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

     @ManyToOne
     @JoinColumn(name = "compartment_id", nullable = true)
     private Compartment compartment;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    @NotNull(message = "Carriage is required")
    @Valid
    private Carriage carriage;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    @NotNull(message = "Seat Type is required")
    @Valid
    private SeatType seatType;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "compartment_number", nullable = true)
    
    private Integer compartmentNumber;

    private boolean status;
}