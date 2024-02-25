package com.finalproject.carpool.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class FeedbackRequest {
    @NotNull(message = "Feedback can't be empty")
    @Size(min = 4, max = 32, message = "Feedback must be between 4 and 32 symbols.")
    private String text;
    @NotNull(message = "Rating can't be empty")
    @Range(min = 0, max = 5, message = "Value must be between 0.0 and 5.0")
    private int rating;

    public FeedbackRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
