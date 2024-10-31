package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "seat_types")
@Data
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    @NotNull(message = "Seat Type ID is required")
    private Long seatTypeId;

    @Column(name = "seat_type", nullable = false)
    private String seatType;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
}