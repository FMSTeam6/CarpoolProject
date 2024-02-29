package com.finalproject.carpool.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.carpool.services.AdditionalOptionsService;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private int id;

    @Column(name = "starting_location", nullable = false)
    private String startingLocation;

    @Column(name = "end_location", nullable = false)
    private String endLocation;

    @Column(name = "empty_seats", nullable = false)
    private int emptySeats;

    @Column(name = "date_of_departure", nullable = false)
    private LocalDateTime dateOfDeparture;

    @Column(name = "price_per_person", nullable = false)
    private double pricePerPerson;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "is_canceled")
    private boolean isCanceled;
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driverId;
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "options_id")
    private AdditionalOptions optionsId;
    @JsonIgnore
    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    private List<User> passengers;
    @JsonIgnore
    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    private List<User> candidatesPool;
    @JsonIgnore
    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;

    public Travel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public void setEmptySeats(int emptySeats) {
        this.emptySeats = emptySeats;
    }

    public LocalDateTime getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(LocalDateTime dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public User getDriverId() {
        return driverId;
    }

    public void setDriverId(User driverId) {
        this.driverId = driverId;
    }

    public AdditionalOptions getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(AdditionalOptions optionsId) {
        this.optionsId = optionsId;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public List<User> getCandidatesPool() {
        return candidatesPool;
    }

    public void setCandidatesPool(List<User> candidatesPool) {
        this.candidatesPool = candidatesPool;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
