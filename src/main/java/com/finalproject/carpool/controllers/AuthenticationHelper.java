package com.finalproject.carpool.controllers;

import com.finalproject.carpool.exceptions.AuthenticationFailureException;
import com.finalproject.carpool.exceptions.EntityNotFoundException;
import com.finalproject.carpool.exceptions.UnauthorizedOperationException;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.LoginRequest;
import com.finalproject.carpool.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String RESOURCE_REQUIRES_AUTHENTICATION = "The request resource requires authentication!";
    private static final String INVALID_AUTHENTICATION = "Wrong username or password.";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException(RESOURCE_REQUIRES_AUTHENTICATION);
        }
        try {
            String userinfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            assert userinfo != null;
            String username = getUsername(userinfo);
            String password = getPassword(userinfo);
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_AUTHENTICATION);
        }
    }

    private String getUsername(String userinfo) {
        int firstSpaceIndex = userinfo.indexOf(" ");
        if (firstSpaceIndex == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
        }
        return userinfo.substring(0, firstSpaceIndex);
    }

    private String getPassword(String userinfo) {
        int firstSpaceIndex = userinfo.indexOf(" ");
        if (firstSpaceIndex == -1) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION);
        }
        return userinfo.substring(firstSpaceIndex + 1);
    }

    public User tryAuthenticateUser(LoginRequest loginRequest) {
        try {
            User user = userService.getByUsername(loginRequest.getUsername());

            if (!user.getPassword().equals(loginRequest.getPassword())) {
                throw new AuthenticationFailureException(INVALID_AUTHENTICATION);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(INVALID_AUTHENTICATION);
        }
    }

    public User tryGetUserFromSession(HttpSession session) {
        try {
            return userService.getByUsername((String) session.getAttribute("currentUser"));
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(INVALID_AUTHENTICATION);
        }
    }
}
