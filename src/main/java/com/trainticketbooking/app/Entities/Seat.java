package com.trainticketbooking.app.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "floor", nullable = true)
    private Integer floor;

    @Column(name = "compartment_number", nullable = true)
    private Integer compartmentNumber;

    private String status;

//    @JsonIgnore
//    @OneToMany(mappedBy = "seat")
//    private List<CarriageSeatMapping> carriageSeatMappings;

    @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    private Carriage carriage;
}