package com.trainticketbooking.app.Dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class BookingFormDto {
    @NotNull(message = "Departure station is required")
    private Integer departureStationId;

    @NotNull(message = "Destination station is required")
    private Integer destinationStationId;

    @NotNull(message = "Departure date is required")
//    @Future(message = "Departure date must be in the future")
    private LocalDate departureDate;

    private LocalDate returnDate;

    @NotNull(message = "Trip type is required")
    @Size(min = 1, message = "Please select a trip type")
    private String tripType;
}
