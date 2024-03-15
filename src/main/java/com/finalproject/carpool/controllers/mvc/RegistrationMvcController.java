package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.mappers.UserMapper;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.EmailVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class RegistrationMvcController {

    private final UserMapper userMapper;

    private final EmailVerificationService emailVerificationService;

    @Autowired
    public RegistrationMvcController(UserMapper userMapper, EmailVerificationService emailVerificationService) {
        this.userMapper = userMapper;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("user") UserRequest userRequest,
                                     HttpServletRequest request) {
        User registeredUser = userMapper.fromRequest(userRequest);
        String siteUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        emailVerificationService.sendVerificationEmail(registeredUser, siteUrl);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration-success");
        return (modelAndView);
    }
}
