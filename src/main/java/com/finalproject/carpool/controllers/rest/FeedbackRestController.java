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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Get all feedbacks", description = "Retrieve all the available feedbacks")
    @GetMapping
    public List<Feedback> get() {
        return feedbackService.getAllFeedbacks();
    }
    @GetMapping("/travels/{travelId}")
    @Operation(summary = "Get feedback by travel ID", description = "Retrieve travel information based on the feedbacks by travel ID")
    public List<Feedback> getFeedbacksFromTravel(@Parameter(description = "Travel ID", example = "1", required = true)
            @PathVariable int travelId) {
        try {
            return feedbackService.getFeedbacksFromTravel(travelId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/authors/{authorId}")
    @Operation(summary = "Get feedback by author ID", description = "Retrieve user information based on the given feedbacks by user ID")
    public List<Feedback> getFeedbacksFromAuthor(@Parameter(description = "User(author) ID", example = "5", required = true)
            @PathVariable int authorId) {
        try {
            return feedbackService.getAllFeedbacksByAuthor(authorId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/recipients/{recipientId}")
    @Operation(summary = "Get feedback by recipient ID", description = "Retrieve user information based on the received feedbacks user ID")
    public List<Feedback> getFeedbacksByRecipient(@Parameter(description = "User(recipient) ID", example = "1", required = true)
            @PathVariable int recipientId) {
        try {
            return feedbackService.getAllFeedbacksByRecipient(recipientId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @Operation(summary = "Create Feedback",
            description = "Create a new feedback for a travel.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Feedback created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Feedback.class))),
                    @ApiResponse(responseCode = "404", description = "Travel not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
                    @ApiResponse(responseCode = "409", description = "Invalid rating")
            })
    @SecurityRequirement(name = "Authorization")
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
        } catch (NotAValidRatingException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @Operation(summary = "Update Feedback",
            description = "Update an existing feedback.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Feedback updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Feedback.class))),
                    @ApiResponse(responseCode = "404", description = "Feedback not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized operation")
            })
    @SecurityRequirement(name = "Authorization")
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
    @Operation(summary = "Delete Feedback",
            description = "Delete an existing feedback.")
    @SecurityRequirement(name = "Authorization")
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
