package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "passenger_types")
@Entity
@Data
public class PassengerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerTypeId;

    private String passengerType;

    private Double discountPercentage;

    @OneToMany(mappedBy = "passengerType")
    private List<Passenger> passengers;
}