package com.finalproject.carpool.models.filters;

import java.util.Optional;

public class SearchUser {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> phoneNumber;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public SearchUser(String username,
                      String email,
                      String phoneNumber,
                      String sortBy,
                      String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public SearchUser() {
        this.username = Optional.empty();
        this.email = Optional.empty();
        this.phoneNumber = Optional.empty();
        this.sortBy = Optional.empty();
        this.sortOrder = Optional.empty();
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
