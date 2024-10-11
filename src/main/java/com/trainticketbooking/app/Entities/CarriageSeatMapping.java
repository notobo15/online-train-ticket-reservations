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
    private Long carriageSeatId;

    @ManyToOne
    @JoinColumn(name = "carriage_id", nullable = false)
    private Carriage carriage;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "carriageSeatMapping")
    private List<Ticket> tickets;
}
