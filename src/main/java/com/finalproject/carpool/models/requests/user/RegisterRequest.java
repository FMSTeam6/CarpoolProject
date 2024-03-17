package com.finalproject.carpool.models.requests.user;

import com.finalproject.carpool.models.requests.LoginRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterRequest extends LoginRequest {
    @NotEmpty(message = "Password confirmation can't be empty.")
    private String passwordConfirm;
    @NotEmpty(message = "First name confirmation can't be empty.")
    @Size(min=2,max = 20, message = "First name must be between 4 and 32 symbols")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty.")
    @Size(min=2,max = 20, message = "Last name must be between 4 and 32 symbols")
    private String lastName;
    @Email(message = "Email must be valid.")
    @NotEmpty(message = "Email name can't be empty.")
    private String email;

    @Size(min = 10, message = "Phone number must be 10 symbols.")
    private String phoneNumber;

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
