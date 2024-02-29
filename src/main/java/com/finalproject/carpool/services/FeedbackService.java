package com.finalproject.carpool.services;

import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksFromTravel(int travelId);

    List<Feedback> getAllFeedbacksByAuthor(int authorId);

    List<Feedback> getAllFeedbacksByRecipient(int recipientId);

    Feedback get(int id);

    Feedback create(Feedback feedback, User user, Travel travel);

    Feedback update(Feedback feedback, User user, Travel travel);

    void delete(int id, User user, Travel travel);

    void rate(Feedback feedback, int rate, User user);
}
