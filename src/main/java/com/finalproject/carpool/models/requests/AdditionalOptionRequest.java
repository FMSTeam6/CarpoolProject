package com.finalproject.carpool.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdditionalOptionRequest {
    @NotNull(message = "Additional option can't be empty")
    @Size(min = 4, max = 32, message = "Additional option must be between 2 and 28 symbols.")
    private String additionalOptions;

    public AdditionalOptionRequest() {
    }

    public String getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(String additionalOptions) {
        this.additionalOptions = additionalOptions;
    }
}
