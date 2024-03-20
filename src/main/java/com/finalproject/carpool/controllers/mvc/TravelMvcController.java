package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.*;
import com.finalproject.carpool.mappers.TravelMapper;
import com.finalproject.carpool.models.AdditionalOptions;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.filters.TravelFilterDto;
import com.finalproject.carpool.models.filters.TravelFilterOptions;
import com.finalproject.carpool.models.requests.TravelRequest;
import com.finalproject.carpool.services.AdditionalOptionsService;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
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

@Controller
@RequestMapping("/travel")
public class TravelMvcController {

    private final TravelService travelService;
    private final UserService userService;
    private final AdditionalOptionsService additionalOptionsService;
    private final TravelMapper travelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TravelMvcController(TravelService travelService, UserService userService, AdditionalOptionsService additionalOptionsService, TravelMapper travelMapper, AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.userService = userService;
        this.additionalOptionsService = additionalOptionsService;
        this.travelMapper = travelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("additionalOptions")
    public List<AdditionalOptions> choiceAdditionOptions() {
        return additionalOptionsService.getAll();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    /*
    Get ALl active Travel
     */
    @GetMapping
    public String showAllTravel(@ModelAttribute("travelOptionDto") TravelFilterDto travelFilterDto, Model model) {
        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                travelFilterDto.getStartingLocation(),
                travelFilterDto.getEndLocation(),
                travelFilterDto.getPricePerPerson(),
                travelFilterDto.getSortBy(),
                travelFilterDto.getOrderBy()
        );
        model.addAttribute("travels", travelService.getAll(travelFilterOptions));
        return "allTravelView";
    }

    /*
    Get All Travel from User
     */
    @GetMapping("/userCreateTravel")
    public String showUserTravel(@ModelAttribute("travelOptionDto") TravelFilterDto travelFilterDto, Model model) {
        TravelFilterOptions travelFilterOptions = new TravelFilterOptions(
                travelFilterDto.getStartingLocation(),
                travelFilterDto.getEndLocation(),
                travelFilterDto.getPricePerPerson(),
                travelFilterDto.getSortBy(),
                travelFilterDto.getOrderBy()
        );
        model.addAttribute("travels", travelService.getAll(travelFilterOptions));
        model.addAttribute("user");
        return "userTravelView";
    }

     /*
    Get travel By id
     */

    @GetMapping("/{id}")
    public String getTravelById(@PathVariable int id, Model model, HttpSession session) {
        try {
            model.addAttribute("travel", travelService.getById(id));
            return "travelView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

     /*
    Create travel
     */

    @GetMapping("/new")
    public String createTravel(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("travel", new TravelRequest());
        return "createTravelVIew";
    }

    @PostMapping("/new")
    public String createTravel(@Valid @ModelAttribute("travel") TravelRequest travelRequest,
                               BindingResult errors, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        if (errors.hasErrors()) {
            return "createTravelView";
        }
        try {
            Travel travel = travelMapper.fromTravelRequest(travelRequest);
            travelService.create(travel, user);
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (LocationNotFoundException e) {
            errors.rejectValue("location", "not_found_location", e.getMessage());
            return "createTravelView";
        }
    }
  /*
    Update Travel
     */

    @GetMapping("/update/{id}")
    public String updateTravel(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            Travel travel = travelService.getById(id);
            TravelRequest travelRequest = travelMapper.fromTravel(travel);
            model.addAttribute("travel", travelRequest);
            return "updateTravelView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/update/{id}")
    public String updateTravel(@PathVariable int id, @Valid @ModelAttribute("travel") TravelRequest travelRequest,
                               BindingResult errors, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (errors.hasErrors()) {
            return "updateTravelView";
        }
        try {
            Travel travel = travelMapper.fromTravelRequest(id, travelRequest);
            travelService.modify(travel, user);
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (LocationNotFoundException e) {
            errors.rejectValue("location", "not_found_location", e.getMessage());
            return "updateTravelView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    /*
    Delete Travel
     */

    @GetMapping("/delete/{id}")
    public String deleteTravel(@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            travelService.delete(id, user);
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    /*
    Show all trips by creator
     */

    @GetMapping("/driver/{driverId}")
    public String getTravelByDriver(@PathVariable int driverId, Model model) {

        try {
            model.addAttribute("travel", travelService.findAllTravelByDriver(driverId));
            return "driverTravel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }
    /*
    Candidate user by travel
     */

    @GetMapping("/candidateUser/{travelId}")
    public String addUserCandidatesPull(@PathVariable int travelId, Model model,
                                        HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            Travel travel = travelService.getById(travelId);
            model.addAttribute("travel", travelService.getById(travelId));
            travelService.candidateTravel(user, travel);
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (UserPassengerAndCandidateException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }



    /*
    Travel completed by id
     */

    @GetMapping("/completed/{id}")
    public String completedTravelById(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Travel travel = travelService.getById(id);
            model.addAttribute("travel", travelService.completedTravel(travel, user));
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (UnsupportedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    /*
    Travel canceled by id
     */

    @GetMapping("/canceled/{id}")
    public String canceledTravelById(Model model, HttpSession session, @PathVariable int id) {
        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Travel travel = travelService.getById(id);
            model.addAttribute("travel", travelService.cancelTravel(travel, user));
            return "redirect:/travel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (UnsupportedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    /*
    Get all Completed
     */
    @GetMapping("/completed")
    public String completedTravelUser(Model model) {
        try {
            model.addAttribute("travels", travelService.completeALLTravel());
            return "completedTravelView";
        }catch (TravelStatusException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    /*
    Get all Canceled
    */
    @GetMapping("/canceled")
    public String canceledTravelUser(HttpSession session, Model model) {
        try {
            model.addAttribute("travels", travelService.cancelALlTravel());
            return "canceledTravelView";
        } catch (TravelStatusException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    /*
    Candidate user by travel from Id
     */

    @GetMapping("/candidate/{travelId}")
    public String viewCandidateForTravel(Model model, @PathVariable int travelId) {
        try {
            model.addAttribute("travels", travelService.getCandidateTravel(travelId));
            model.addAttribute("travel", travelService.getById(travelId));
            travelService.getCandidateTravel(travelId);
            return "candidateTravelView";
        } catch (UnsupportedOperationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    /*
    Creator travel choice passenger
     */

    @GetMapping("/driverChoice/{travelId}/{userId}")
    public String choiceDriverPassenger(@PathVariable int travelId, @PathVariable int userId, Model model) {
        try {
            Travel travel = travelService.getById(travelId);
            User user = userService.getById(userId);
            model.addAttribute("travel", travel);
            model.addAttribute("user", user);
            travelService.addPassengerToTravel(user, travel);

            return "redirect:/travel/candidate/{travelId}";
        }catch (TravelNotEmptySeats e){
            model.addAttribute("statusCode", HttpStatus.GONE.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }catch (UserPassengerAndCandidateException e){
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

    }

}
