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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    @NotNull(message = "Seattype is required!")
    private SeatType seatType;

    @Column(name = "seat_number", nullable = false)
    @NotEmpty(message = "SeatNumber is required!")
    private String seatNumber;

    @Column(name = "floor", nullable = true)
    @NotNull(message = "Floor is required!")
    private Integer floor;

    @Column(name = "compartment_number", nullable = true)
    @NotNull(message = "CompartmentNumber is required!")
    private Integer compartmentNumber;

    @NotNull(message = "Status is required!")
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    @OneToMany(mappedBy = "seat")
    private List<CarriageSeatMapping> carriageSeatMappings;
}