package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.requests.FeedbackRequest;
import com.finalproject.carpool.services.FeedbackService;
import com.finalproject.carpool.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {
    private final FeedbackService feedbackService;

    private final TravelService travelService;

    @Autowired
    public FeedbackMapper(FeedbackService feedbackService, TravelService travelService) {
        this.feedbackService = feedbackService;
        this.travelService = travelService;
    }

    public Feedback fromRequest(int id, FeedbackRequest request) {
        Feedback feedback = fromRequest(request);
        feedback.setId(id);
        Feedback repositoryFeedback = feedbackService.get(id);
        feedback.setAuthorId(repositoryFeedback.getAuthorId());
        return feedback;
    }

    public Feedback fromRequest(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setText(request.getText());
        feedback.setRating(request.getRating());
        //  feedback.setRecipientId(travelService.getDriverId());
        return feedback;
    }

    public FeedbackRequest toRequest(Feedback feedback) {
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setText(feedback.getText());
        feedbackRequest.setRating(feedback.getRating());
        return feedbackRequest;
    }
}
