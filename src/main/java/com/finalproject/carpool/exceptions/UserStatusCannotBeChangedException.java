package com.finalproject.carpool.exceptions;

public class UserStatusCannotBeChangedException extends RuntimeException{

    public UserStatusCannotBeChangedException(String type, String status) {
        super(String.format("User %s is already %s.",type,status));
    }
}
