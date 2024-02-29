package com.finalproject.carpool.models.requests.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;


public class UserRequest {
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols.")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols.")
    private String lastName;

    @Email
    private String email;

    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    private String username;

    @Size(min = 8, message = "Password must be more than 8 symbols.")
    private String password;

    @Size(min = 10, message = "Phone number must be 10 symbols.")
    private String phoneNumber;

    public UserRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
