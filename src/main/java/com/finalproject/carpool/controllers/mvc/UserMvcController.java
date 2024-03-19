package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.AuthenticationFailureException;
import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.mappers.UserMapper;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;

    private final UserService userService;

    private final UserMapper userMapper;
    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/update")
    public String updatePost(Model model, HttpSession session) {
        try {
            model.addAttribute("currentUser", authenticationHelper.tryGetUserFromSession(session));
            return "userUpdateView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }
    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("currentUser") UserRequest request,
                             BindingResult bindingResult,
                             Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "userUpdateView";
        }

        try {
            User userToUpdate = userMapper.fromRequest(user.getId(), request);
            userService.update(userToUpdate, userToUpdate.getId(),user.getId());
            model.addAttribute("currentUser",user);
            return "userPageView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "userUpdateView";
        }
    }

//    @GetMapping("/{userId}")
//    public String showSingleUser(@PathVariable int userId, Model model) {
//        try {
//            model.addAttribute("user", userService.getById(userId));
//            return "UserView";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//        }
//        return "ErrorView";
//    }


}
