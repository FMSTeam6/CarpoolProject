package com.finalproject.carpool.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeMvcController {

    @GetMapping
    public String home(){
        return "index";
    }
}
