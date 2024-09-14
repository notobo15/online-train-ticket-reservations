package com.trainticketbooking.app.Entities;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "train_cars")
@Data
public class TrainCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "car_type", nullable = false)
    private String carType;

    @Column(name = "total_floors")
    private int totalFloors;

    @OneToMany(mappedBy = "trainCar")
    private List<Compartment> compartments;

    @Override
    public String toString() {
        return "TrainCar{" +
                "carId=" + carId +
                ", train=" + train +
                ", carNumber='" + carNumber + '\'' +
                ", carType='" + carType + '\'' +
                ", totalFloors=" + totalFloors +
                ", compartments=" + compartments +
                '}';
    }
}