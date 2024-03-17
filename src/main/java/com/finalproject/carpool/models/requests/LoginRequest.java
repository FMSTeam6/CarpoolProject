package com.finalproject.carpool.models.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2, max = 20, message = "username must be between 2 and 20 symbols")
    private String username;
    @NotEmpty(message = "Password can't be empty.")
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
