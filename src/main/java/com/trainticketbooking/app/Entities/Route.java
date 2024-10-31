package com.trainticketbooking.app.Entities;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "routes")
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @NotNull(message = "Start station cannot be null")
    @ManyToOne
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @NotNull(message = "End station cannot be null")
    @ManyToOne
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;

    @NotNull(message = "Distance cannot be null")
    @Min(value = 0, message = "Distance >= 0")
    @Column(name = "distance", nullable = false)
    private Double distance;

    @NotNull(message = "Arrival time cannot be null")
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @NotNull(message = "Departure time cannot be null")
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Min(value = 0, message = "Station >= 0")
    @Column(name = "station_number")
    private int stationNumber;

    @NotBlank(message = "Status cannot be blank")
    @Column(name = "status")
    private String status;  // Active, Maintenance, Suspended

    @Min(value = 0, message = "Date >= 0")
    @Column(name = "date_number")
    private int dateNumber;

    @NotNull(message = "Train cannot be null")
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", startStationId=" + (startStation != null ? startStation.getStationId() : "N/A") +
                ", endStationId=" + (endStation != null ? endStation.getStationId() : "N/A") +
                ", distance=" + distance +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", stationNumber=" + stationNumber +
                ", status='" + status + '\'' +
                ", dateNumber=" + dateNumber +
                ", trainId=" + (train != null ? train.getTrainId() : "N/A") +
                '}';
    }
}