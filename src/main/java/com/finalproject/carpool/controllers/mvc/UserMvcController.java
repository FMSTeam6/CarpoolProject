package com.finalproject.carpool.controllers.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserMvcController {


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
