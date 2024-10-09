package com.trainticketbooking.app.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "stations")
@Data
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stationId;

    @Column(name = "station_name")
    private String stationName;

    @ManyToOne
    @JoinColumn(name = "province_code")
    private Province provinceCode;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude; // Vĩ độ

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude; // Kinh độ

    @OneToMany(mappedBy = "startStation")
    private List<Route> startRoutes;

    @OneToMany(mappedBy = "endStation")
    private List<Route> endRoutes;
}