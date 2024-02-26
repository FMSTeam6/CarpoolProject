package com.finalproject.carpool.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class TravelRequest {
    @NotNull(message = "Starting Location can't be empty.")
    private String StartingLocation;
    @NotNull(message = "End Location can't be empty.")
    private String EndLocation;
    @Positive(message = "Seats must be positive.")
    private int EmptySeats;
    @NotNull(message = "Choose validate date and time")
    private LocalDateTime DateOfDeparture;
    @Positive(message = "Price must be positive")
    private Double PricePerPerson;

    public TravelRequest() {
    }

    public String getStartingLocation() {
        return StartingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        StartingLocation = startingLocation;
    }

    public String getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(String endLocation) {
        EndLocation = endLocation;
    }

    public int getEmptySeats() {
        return EmptySeats;
    }

    public void setEmptySeats(int emptySeats) {
        EmptySeats = emptySeats;
    }

    public LocalDateTime getDateOfDeparture() {
        return DateOfDeparture;
    }

    public void setDateOfDeparture(LocalDateTime dateOfDeparture) {
        DateOfDeparture = dateOfDeparture;
    }

    public Double getPricePerPerson() {
        return PricePerPerson;
    }

    public void setPricePerPerson(Double pricePerPerson) {
        PricePerPerson = pricePerPerson;
    }
}
