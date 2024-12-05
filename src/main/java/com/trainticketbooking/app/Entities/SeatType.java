package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "seat_types")
@Data
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    private Long seatTypeId;

    @Column(name = "seat_type", nullable = false)
    @NotEmpty(message = "SeatType is required!")
    private String seatType;

    @Column(name = "code")
    @NotEmpty(message = "Code is required!")
    private String code;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "seatType")
    private Price price;

}