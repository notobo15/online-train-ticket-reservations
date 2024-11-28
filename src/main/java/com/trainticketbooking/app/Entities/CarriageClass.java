package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "carriage_class")
public class CarriageClass {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "carriage_class_id")
        private Integer carriageClassId;

        @NotEmpty(message = "name is required!")
        private String name;

        @OneToMany(mappedBy = "carriageClass")
        private List<Carriage> carriages;
}
