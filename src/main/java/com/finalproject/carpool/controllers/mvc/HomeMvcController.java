package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.SearchUser;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.services.FeedbackService;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeMvcController {
private final UserService userService;
private final FeedbackService feedbackService;
private final TravelService travelService;

    public HomeMvcController(UserService userService, FeedbackService feedbackService, TravelService travelService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
        this.travelService = travelService;
    }

    @GetMapping
    public String home(Model model){
        List<User> users = userService.getAll(new SearchUser());
        model.addAttribute("topUsers", users);
        List<Feedback> feedbackTop = feedbackService.getAllFeedbacksByRecipient(1);
        model.addAttribute("feedbackTop", feedbackTop);
        List<Feedback> feedbackSecond = feedbackService.getAllFeedbacksByRecipient(2);
        model.addAttribute("feedbackSecond", feedbackSecond);
        List<Feedback> feedbackThree = feedbackService.getAllFeedbacksByRecipient(2);
        model.addAttribute("feedbackThree", feedbackThree);
        List<Travel> travels = travelService.completeALLTravel();
        model.addAttribute("travels", travels);
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbacks);
        return "index";
    }
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}
