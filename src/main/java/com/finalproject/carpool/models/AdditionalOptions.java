package com.finalproject.carpool.models;

import jakarta.persistence.*;

@Entity
@Table(name = "additional_options")
public class AdditionalOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_id")
    private boolean optionsId;

    @Column(name = "luggage")
    private boolean luggage;

    @Column(name = "smoking")
    private boolean smoking;

    @Column(name = "eating")
    private boolean eating;

    @Column(name = "pets")
    private boolean pets;

    @Column(name = "air_condition")
    private boolean airCondition;

    @Column(name = "power_outlet")
    private boolean powerOutlet;

    @Column(name = "reclining_seats")
    private boolean recliningSeats;

    public AdditionalOptions() {
    }

    public boolean isOptionsId() {
        return optionsId;
    }

    public void setOptionsId(boolean optionsId) {
        this.optionsId = optionsId;
    }

    public boolean isLuggage() {
        return luggage;
    }

    public void setLuggage(boolean luggage) {
        this.luggage = luggage;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isEating() {
        return eating;
    }

    public void setEating(boolean eating) {
        this.eating = eating;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isAirCondition() {
        return airCondition;
    }

    public void setAirCondition(boolean airCondition) {
        this.airCondition = airCondition;
    }

    public boolean isPowerOutlet() {
        return powerOutlet;
    }

    public void setPowerOutlet(boolean powerOutlet) {
        this.powerOutlet = powerOutlet;
    }

    public boolean isRecliningSeats() {
        return recliningSeats;
    }

    public void setRecliningSeats(boolean recliningSeats) {
        this.recliningSeats = recliningSeats;
    }
}
