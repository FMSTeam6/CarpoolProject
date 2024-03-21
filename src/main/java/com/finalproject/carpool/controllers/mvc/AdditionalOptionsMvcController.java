package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.AuthenticationFailureException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.mappers.AdditionalOptionMapper;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.AdditionalOptionRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/additional-options")
public class AdditionalOptionsMvcController {

    private final AdditionalOptionsService additionalOptionsService;
    private final AdditionalOptionMapper additionalOptionMapper;

    private final AuthenticationHelper authenticationHelper;

    public AdditionalOptionsMvcController(AdditionalOptionsService additionalOptionsService, AdditionalOptionMapper additionalOptionMapper, AuthenticationHelper authenticationHelper) {
        this.additionalOptionsService = additionalOptionsService;
        this.additionalOptionMapper = additionalOptionMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping()
    public String showAllTravels(Model model) {
        model.addAttribute("additionalOptions", additionalOptionsService.getAll());
        return "additionalOptionsView";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("additionalOption", new AdditionalOptionRequest());
        return "createAdditionalOptionsVIew";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("additionalOptions") AdditionalOptionRequest additionalOptionRequest, BindingResult errors, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (errors.hasErrors()) {
            return "createAdditionalOptionsVIew";
        }
        AdditionalOptions additionalOptions = additionalOptionMapper.fromRequest(additionalOptionRequest);
        additionalOptionsService.create(additionalOptions,user);
        return "redirect:/additional-options";

    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            AdditionalOptions additionalOptions = additionalOptionsService.get(id);
            AdditionalOptionRequest additionalOptionRequest = additionalOptionMapper.fromRequest(additionalOptions);
            model.addAttribute("additionalOption", additionalOptionRequest);
            return "updateAdditionalOptionsView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable int id, @Valid @ModelAttribute("additionalOptions") AdditionalOptionRequest additionalOptionRequest, BindingResult errors, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (errors.hasErrors()) {
            return "updateAdditionalOptionsView";
        }
        try {
            AdditionalOptions additionalOptions = additionalOptionMapper.fromRequest(id,additionalOptionRequest);
            additionalOptionsService.update(additionalOptions, user);
            return "redirect:/additional-options";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
           additionalOptionsService.delete(id, user);
            return "redirect:/additional-options";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
