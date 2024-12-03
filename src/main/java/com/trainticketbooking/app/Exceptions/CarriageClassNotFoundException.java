package com.trainticketbooking.app.Exceptions;

public class CarriageClassNotFoundException extends RuntimeException{
    public CarriageClassNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
