package com.finalproject.carpool.exceptions;

public class NotAValidRatingException extends RuntimeException{
    public NotAValidRatingException(String message) {
        super(message);
    }
}
