package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.mappers.UserMapper;
import com.finalproject.carpool.models.Feedback;
import com.finalproject.carpool.models.Travel;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.FeedbackRequest;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK =
            "Sorry you are blocked and you can not create content. " +
                    "For more info please contact one of the admins!";
    private final AuthenticationHelper authenticationHelper;

    private final UserService userService;
    private final TravelService travelService;

    private final UserMapper userMapper;

    @Autowired
    public UserRestController(AuthenticationHelper authenticationHelper, UserService userService, TravelService travelService, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelService = travelService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
        try {
            User user = userMapper.fromRequest(request);
            return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
