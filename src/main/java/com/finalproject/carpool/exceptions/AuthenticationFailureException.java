package com.finalproject.carpool.exceptions;

public class AuthenticationFailureException extends RuntimeException{

    public AuthenticationFailureException(String message){
        super(message);
    }
}
