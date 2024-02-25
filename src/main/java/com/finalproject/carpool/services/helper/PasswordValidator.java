package com.finalproject.carpool.services.helper;

public class PasswordValidator {
    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (!password.matches(".*[@#$%^&!].*")) {
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            return false;
        }

        return true;
    }
}


