package com.finalproject.carpool.repositories;

import com.finalproject.carpool.models.Feedback;

import java.util.List;

public interface FeedbackRepository {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksFromTravel(int travelId);

    List<Feedback> getAllFeedbacksByAuthor(int authorId);

    List<Feedback> getAllFeedbacksByRecipient(int recipientId);

    Feedback get(int id);

    void createFeedback(Feedback feedback);

    void updateFeedback(Feedback feedback);

    void deleteFeedback(int id);

    void rate(int id, int rate);

}
