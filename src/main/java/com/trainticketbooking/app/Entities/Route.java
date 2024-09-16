package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @ManyToOne
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @ManyToOne
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @ManyToOne
    @JoinColumn(name = "railway_route_id")
    private RailwayRoute railwayRoute;

    @Column(name = "station_number")
    private int stationNumber;

    @Column(name = "date_number")
    private int dateNumber;

    @OneToMany(mappedBy = "route")
    private List<SeatRoute> seatRoutes;
}