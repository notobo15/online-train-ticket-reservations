package com.trainticketbooking.app.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne()
    @JoinColumn(name = "carriage_class_Id")
    @JsonIgnore
    private CarriageClass carriageClass;

    @Column(name = "carriage_number", nullable = false)
    private String carriageNumber;

    @Column(name = "seat_count", nullable = false)
    private int seatCount;

    @Column(name = "total_floors")
    private int totalFloors;

//    @OneToMany(mappedBy = "carriage")
//    @JsonIgnore
//    private List<CarriageSeatMapping> carriageSeatMappings;


    @OneToMany(mappedBy = "carriage")
    private List<Seat> seats;
}