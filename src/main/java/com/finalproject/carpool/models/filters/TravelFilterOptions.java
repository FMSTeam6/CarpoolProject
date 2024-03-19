package com.finalproject.carpool.models.filters;

import java.time.LocalDateTime;
import java.util.Optional;

public class TravelFilterOptions {
    private Optional<String> startingLocation;
    private Optional<String> endLocation;
    private Optional<Double> pricePerPerson;
    private Optional<String> sortBy;
    private Optional<String> orderBy;

    public TravelFilterOptions(String startingLocation,
                               String endLocation,
                               Double pricePerPerson,
                               String sortBy,
                               String orderBy) {
        this.startingLocation = Optional.ofNullable(startingLocation);
        this.endLocation = Optional.ofNullable(endLocation);
        this.pricePerPerson = Optional.ofNullable(pricePerPerson);
        this.sortBy = Optional.ofNullable(sortBy);
        this.orderBy = Optional.ofNullable(orderBy);
    }

    public TravelFilterOptions(){
        this.startingLocation = Optional.empty();
        this.endLocation = Optional.empty();
        this.pricePerPerson = Optional.empty();
        this.orderBy = Optional.empty();
        this.orderBy = Optional.empty();
    }

    public Optional<String> getStartingLocation() {
        return startingLocation;
    }

    public Optional<String> getEndLocation() {
        return endLocation;
    }

    public Optional<Double> getPricePerPerson() {
        return pricePerPerson;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }
}
