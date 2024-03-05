package com.finalproject.carpool.models.filters;

import java.time.LocalDateTime;
import java.util.Optional;

public class TravelFilterOptions {
    private Optional<String> startLocation;
    private Optional<String> endLocation;
    private Optional<LocalDateTime> dateOfDeparture;
    private Optional<Double> pricePerPerson;
    private Optional<Integer> driver;
    private Optional<String> sortBy;
    private Optional<String> orderBy;

    public TravelFilterOptions(String startLocation,
                               String endLocation,
                               LocalDateTime dateOfDeparture,
                               Double pricePerPerson,
                               Integer driver,
                               String sortBy,
                               String orderBy) {
        this.startLocation = Optional.ofNullable(startLocation);
        this.endLocation = Optional.ofNullable(endLocation);
        this.dateOfDeparture = Optional.ofNullable(dateOfDeparture);
        this.pricePerPerson = Optional.ofNullable(pricePerPerson);
        this.driver = Optional.ofNullable(driver);
        this.sortBy = Optional.ofNullable(sortBy);
        this.orderBy = Optional.ofNullable(orderBy);
    }

    public TravelFilterOptions(){
        this.startLocation = Optional.empty();
        this.endLocation = Optional.empty();
        this.dateOfDeparture = Optional.empty();
        this.pricePerPerson = Optional.empty();
        this.driver = Optional.empty();
        this.orderBy = Optional.empty();
        this.orderBy = Optional.empty();
    }

    public Optional<String> getStartLocation() {
        return startLocation;
    }

    public Optional<String> getEndLocation() {
        return endLocation;
    }

    public Optional<LocalDateTime> getDateOfDeparture() {
        return dateOfDeparture;
    }

    public Optional<Double> getPricePerPerson() {
        return pricePerPerson;
    }

    public Optional<Integer> getDriver() {
        return driver;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }
}
