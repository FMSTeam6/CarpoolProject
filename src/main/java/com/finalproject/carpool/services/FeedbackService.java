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

    void create(Feedback feedback, User user, Travel travel);

    void update(Feedback feedback, User user);

    void delete(int id, User user);

    void rate(Feedback feedback, int rate, User user);
}
