package com.finalproject.carpool.exceptions;

public class TravelStatusException extends RuntimeException{

    public TravelStatusException(String value) {
        super(String.format("No trips %s.",value));
    }
}
