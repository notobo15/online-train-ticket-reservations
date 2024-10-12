package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "carriage_seat_mapping")
@Data
public class CarriageSeatMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carriage_seat_id")
    private Integer carriageSeatId;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    private Carriage carriage;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "carriageSeatMapping")
    private List<Ticket> tickets;
}
