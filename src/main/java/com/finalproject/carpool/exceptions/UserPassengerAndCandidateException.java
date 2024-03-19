package com.finalproject.carpool.exceptions;

public class UserPassengerAndCandidateException extends RuntimeException{

    public UserPassengerAndCandidateException(String value, String choice) {
        super(String.format("%s already %s",value,choice));
    }
}
