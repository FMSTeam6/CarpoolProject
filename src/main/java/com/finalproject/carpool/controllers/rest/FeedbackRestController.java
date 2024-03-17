package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.NotAValidRatingException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.FeedbackMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.FeedbackRequest;
import com.finalproject.carpool.services.FeedbackService;
import com.finalproject.carpool.services.TravelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackRestController {
    private final AuthenticationHelper authenticationHelper;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackService feedbackService;
    private final TravelService travelService;

    @Autowired
    public FeedbackRestController(AuthenticationHelper authenticationHelper, FeedbackMapper feedbackMapper, FeedbackService feedbackService, TravelService travelService) {
        this.authenticationHelper = authenticationHelper;
        this.feedbackMapper = feedbackMapper;
        this.feedbackService = feedbackService;
        this.travelService = travelService;
    }

    @GetMapping
    public List<Feedback> get() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/travels/{travelId}")
    public List<Feedback> getFeedbacksFromTravel(@PathVariable int travelId) {
        try {
            return feedbackService.getFeedbacksFromTravel(travelId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/authors/{authorId}")
    public List<Feedback> getFeedbacksFromAuthor(@PathVariable int authorId) {
        try {
            return feedbackService.getAllFeedbacksByAuthor(authorId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/recipients/{recipientId}")
    public List<Feedback> getFeedbacksByRecipient(@PathVariable int recipientId) {
        try {
            return feedbackService.getAllFeedbacksByRecipient(recipientId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{travelId}")
    public ResponseEntity<Feedback> createFeedback(@RequestHeader HttpHeaders headers, @PathVariable int travelId,
                                                   @Valid @RequestBody FeedbackRequest request) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getById(travelId);
            Feedback feedback = feedbackMapper.fromRequest(request);
            return new ResponseEntity<>(feedbackService.create(feedback, user, travel), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NotAValidRatingException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Feedback> updateFeedback(@RequestHeader HttpHeaders headers, @PathVariable int feedbackId,
                                                    @Valid @RequestBody FeedbackRequest request) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Feedback feedback = feedbackMapper.fromRequest(feedbackId, request);
            return new ResponseEntity<>(feedbackService.update(feedback, user), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{feedbackId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int feedbackId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            feedbackService.delete(feedbackId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
