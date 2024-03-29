package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.AuthenticationFailureException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.NotAValidRatingException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.FeedbackMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.models.requests.FeedbackRequest;
import com.finalproject.carpool.services.FeedbackService;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/feedback")
public class FeedbackMvcController {
    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;
    private final AuthenticationHelper authenticationHelper;

    private final TravelService travelService;

    private final UserService userService;

    @Autowired
    public FeedbackMvcController(FeedbackService feedbackService, FeedbackMapper feedbackMapper, AuthenticationHelper authenticationHelper, TravelService travelService, UserService userService) {
        this.feedbackService = feedbackService;
        this.feedbackMapper = feedbackMapper;
        this.authenticationHelper = authenticationHelper;
        this.travelService = travelService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    @GetMapping("/showAll")
    public String showAllFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "feedbackView";
    }
    @GetMapping("/user-feedbacks/{recipientId}")
    public String showAllFeedbacks(@PathVariable int recipientId, Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacksByRecipient(recipientId));
        return "feedbackView";
    }
    @GetMapping("/create/{travelId}")
    public String showFeedbackForm(@PathVariable int travelId, Model model) {
        model.addAttribute("users", userService.getAll(new SearchUser()));
        model.addAttribute("feedback", new FeedbackRequest());
        model.addAttribute("travel", travelService.completeALLTravel());
        return "createFeedbackView";
    }

    @PostMapping("/create/{travelId}")
    public String submitFeedback(@Valid @ModelAttribute("feedback") FeedbackRequest request,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session,
                                 @PathVariable int travelId) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        if (errors.hasErrors()) {
            return "createFeedbackView";
        }
        try {
            Travel travel = travelService.getById(travelId);
            Feedback feedback = feedbackMapper.fromRequest(request);
            feedbackService.create(feedback, user, travel);
            return "thank-you";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (NotAValidRatingException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @GetMapping("/update/{feedbackId}")
    public String changeFeedback(@PathVariable int feedbackId, Model model, HttpSession session) {
        try {
        User user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            Feedback feedback = feedbackService.get(feedbackId);
            FeedbackRequest request = feedbackMapper.toRequest(feedback);
            model.addAttribute("feedback", request);
            return "updateOrDeleteFeedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/update/{feedbackId}")
    public String updateFeedback(@PathVariable int feedbackId,
                             @Valid @ModelAttribute("feedback") FeedbackRequest request,
                             BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (bindingResult.hasErrors()) {
            return "updateOrDeleteFeedbackView";
        }
        try {
            Feedback feedback = feedbackMapper.fromRequest(feedbackId, request);
            Travel travel = travelService.getById(1);
            feedbackService.update(feedback,user, travel);
            return "redirect:/feedback/feedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @GetMapping("/delete/{feedbackId}")
    public String deleteFeedback(@PathVariable int feedbackId, Model model,HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }
        try {
            feedbackService.delete(feedbackId, user);
            return "redirect:/index";
        } catch (EntityNotFoundException | AuthenticationFailureException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}




