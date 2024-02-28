package com.finalproject.carpool.models;

import jakarta.persistence.*;

@Entity
@Table(name = "additional_options")
public class AdditionalOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_id")
    private int optionsId;

    @Column(name = "additional_options")
    private String additionalOptions;

    public AdditionalOptions() {
    }

    public int getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(int optionsId) {
        this.optionsId = optionsId;
    }

    public String getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }
}
