package com.finalproject.carpool.services.impl;

import java.util.List;

import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.repositories.FeedbackRepository;
import com.finalproject.carpool.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    public static final String ONLY_AUTHOR_CAN_MODIFY_CONTENT =
            "Only feedback author can modify the content of the feedback!";
    public static final String ONLY_ADMIN_OR_AUTHOR_CAN_DELETE =
            "Only admin or author can delete a feedback!";
    public static final String BLOCKED_USER = "Your account is blocked!";

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.getAllFeedbacks();
    }

    @Override
    public List<Feedback> getFeedbacksFromTravel(int travelId) {
        return feedbackRepository.getFeedbacksFromTravel(travelId);
    }

    @Override
    public List<Feedback> getAllFeedbacksByAuthor(int authorId) {
        return feedbackRepository.getAllFeedbacksByAuthor(authorId);
    }

    @Override
    public List<Feedback> getAllFeedbacksByRecipient(int recipientId) {
        return feedbackRepository.getAllFeedbacksByRecipient(recipientId);
    }

    @Override
    public Feedback get(int id) {
        return feedbackRepository.get(id);
    }

    @Override
    public Feedback create(Feedback feedback, User user, Travel travel) {
        checkIfBlocked(user);
        feedback.setRecipientId(travel.getDriverId());
        feedback.setAuthorId(user);
        feedback.setTravelId(travel);
        travel.getFeedbacks().add(feedback);
        return feedbackRepository.createFeedback(feedback);
    }

    @Override
    public Feedback update(Feedback feedback, User user, Travel travel) {
        checkIfBlocked(user);
        checkUpdatePermissions(feedback, user);
        feedback.setRecipientId(travel.getDriverId());
        feedback.setAuthorId(user);
        feedback.setTravelId(travel);
        return feedbackRepository.updateFeedback(feedback);
    }

    @Override
    public void delete(int id, User user, Travel travel) {
        checkDeletePermissions(feedbackRepository.get(id), user);
        travel.getFeedbacks().remove(feedbackRepository.get(id));
        feedbackRepository.deleteFeedback(id);
    }

    //TODO rating system should be tested in postman
    @Override
    public void rate(Feedback feedback, int rate, User user) {
        checkIfBlocked(user);
        feedbackRepository.rate(feedback.getId(), rate);
    }

    private void checkIfBlocked(User user) {
        if (user.isBanned()) {
            throw new UnauthorizedOperationException(BLOCKED_USER);
        }
    }

    private void checkUpdatePermissions(Feedback feedback, User user) {
        if (!feedback.getAuthorId().equals(user)) {
            throw new UnauthorizedOperationException(ONLY_AUTHOR_CAN_MODIFY_CONTENT);
        }
    }

    private void checkDeletePermissions(Feedback feedback, User user) {
        if (!user.isAdmin()) {
            if (!feedback.getAuthorId().equals(user)) {
                throw new UnauthorizedOperationException(ONLY_ADMIN_OR_AUTHOR_CAN_DELETE);
            }
        }
    }
}

