package com.finalproject.carpool.models.filters;

public class TravelFilterDto {
    private String startingLocation;
    private String endLocation;
    private Double pricePerPerson;
    private String sortBy;
    private String orderBy;

    public TravelFilterDto() {
    }

    public TravelFilterDto(String startingLocation,
                           String endLocation,
                           Double pricePerPerson,
                           String sortBy,
                           String orderBy) {
        this.startingLocation = startingLocation;
        this.endLocation = endLocation;
        this.pricePerPerson = pricePerPerson;
        this.sortBy = sortBy;
        this.orderBy = orderBy;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startingLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }


    public Double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(Double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
