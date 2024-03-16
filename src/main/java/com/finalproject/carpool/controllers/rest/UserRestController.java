package com.finalproject.carpool.controllers.rest;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.exceptions.UserStatusCannotBeChangedException;
import com.finalproject.carpool.mappers.UserMapper;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.TravelService;
import com.finalproject.carpool.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String BLOCKED_USERS_CAN_NOT_GIVE_FEEDBACK =
            "Sorry you are blocked and you can not create content. " +
                    "For more info please contact one of the admins!";

    private static final String UPDATE_USER_ERROR_MESSAGE = "Only owner of the account can update personal info.";

    public static final String YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION =
            "You are not authorized to browse user information";

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

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable int userId){
        try{
            return new ResponseEntity<>(userService.getById(userId), HttpStatus.FOUND);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserRequest request) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToUpdate = userMapper.fromRequest(id, request);
            return new ResponseEntity<>(userService.update(userToUpdate, id, user.getId()), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.deleteUser(id, user.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/ban/{userId}")
    public void ban(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBan = userService.getById(userId);
            userService.banUser(userToBan, user.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (UserStatusCannotBeChangedException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/unban/{userId}")
    public void unBan(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToUnBan = userService.getById(userId);
            userService.unBanUser(userToUnBan, user.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (UserStatusCannotBeChangedException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
