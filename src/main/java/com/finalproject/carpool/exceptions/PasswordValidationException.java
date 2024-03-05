package com.finalproject.carpool.exceptions;

public class PasswordValidationException extends RuntimeException{
    public PasswordValidationException(String message) {
        super(message);
    }
}
