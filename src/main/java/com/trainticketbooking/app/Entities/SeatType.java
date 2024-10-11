package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "seat_types")
@Data
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    private Long seatTypeId;

    @Column(name = "seat_type", nullable = false)
    private String seatType;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;


    @OneToMany(mappedBy = "seatType")
    private List<SeatType> seatTypes;
}