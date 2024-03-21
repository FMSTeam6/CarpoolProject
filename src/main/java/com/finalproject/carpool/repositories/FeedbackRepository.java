package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Feedback;

import java.util.List;

public interface FeedbackRepository {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksFromTravel(int travelId);

    List<Feedback> getAllFeedbacksByAuthor(int authorId);

    List<Feedback> getAllFeedbacksByRecipient(int recipientId);

    Feedback get(int id);

    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Feedback feedback);

    void deleteFeedback(int id);

}
