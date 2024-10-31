package com.trainticketbooking.app.Exceptions;

public class CarriageNotFoundException extends RuntimeException{
    public CarriageNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
