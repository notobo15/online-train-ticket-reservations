package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "carriage_class")
public class CarriageClass {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @OneToMany(mappedBy = "carriageClass")
        private List<Carriage> carriages;
}
