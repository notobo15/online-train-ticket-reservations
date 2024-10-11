package com.trainticketbooking.app.Entities;

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
    private Train train;

    @ManyToOne()
    @JoinColumn(name = "carriage_class_Id")
    private CarriageClass carriageClass;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "set_count", nullable = false)
    private int seatCount;

    @Column(name = "total_floors")
    private int totalFloors;

    @OneToMany(mappedBy = "carriage")
    private List<CarriageSeatMapping> carriageSeatMappings;
}