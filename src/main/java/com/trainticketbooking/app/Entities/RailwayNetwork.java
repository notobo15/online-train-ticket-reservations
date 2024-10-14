package com.trainticketbooking.app.Entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "railway_networks")
@Data
public class RailwayNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "railway_neworks_id")
    private Integer railwayId;

    @Column(name = "name")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @OneToMany(mappedBy = "railwayNetwork")
    private List<Train> trains;

    @Column(name = "status")
    @NotBlank(message = "Status cannot be empty")
    private String status; // Active, Maintenance, Suspended
}

