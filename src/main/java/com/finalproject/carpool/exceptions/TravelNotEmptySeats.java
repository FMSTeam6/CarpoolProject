package com.finalproject.carpool.exceptions;

public class TravelNotEmptySeats extends RuntimeException {

    public TravelNotEmptySeats() {
        super("This travel not empty seats.");
    }
}
