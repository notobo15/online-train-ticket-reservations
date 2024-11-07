package com.trainticketbooking.app.Exceptions;

public class SeatTypeNotFoundException extends RuntimeException{
    public SeatTypeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
