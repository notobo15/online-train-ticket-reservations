package com.trainticketbooking.app.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stations")
@Data
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stationId;

    @Column(name = "name")
    private String stationName;

    @Column(name = "code", nullable = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = true)
    private Province province;

    @OneToMany(mappedBy = "startStation")
    private List<Route> startRoutes;

    @OneToMany(mappedBy = "endStation")
    private List<Route> endRoutes;

    @Column(name = "ggmap_link", nullable = true)
    private String GGMapLink;
}