package com.trainticketbooking.app.Entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "carriages")
@Data
public class Carriage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carriage_id")
    private Integer carriageId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    @NotNull(message = "Train is required!")
    private Train train;

    @ManyToOne()
    @JoinColumn(name = "carriage_class_Id")
    @NotNull(message = "CarriageClass is required!")
    private CarriageClass carriageClass;

    @Column(name = "car_number", nullable = false)
    @NotEmpty(message = "CarNumber is required!")
    private String carNumber;

    @Column(name = "set_count", nullable = false)
    @NotNull(message = "SeatCount is required!")
    private int seatCount;

    @Column(name = "total_floors")
    @NotNull(message = "TotalFloors is required!")
    private int totalFloors;

    @OneToMany(mappedBy = "carriage")
    private List<CarriageSeatMapping> carriageSeatMappings;
}