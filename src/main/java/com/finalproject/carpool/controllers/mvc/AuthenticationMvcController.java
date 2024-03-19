package com.finalproject.carpool.controllers.mvc;

import com.finalproject.carpool.controllers.AuthenticationHelper;
import com.finalproject.carpool.exceptions.AuthenticationFailureException;
import com.finalproject.carpool.exceptions.EntityDuplicateException;
import com.finalproject.carpool.mappers.UserMapper;
import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.LoginRequest;
import com.finalproject.carpool.models.requests.user.RegisterRequest;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private static final String PASSWORD_CONFIRM_NOT_MATCH = "Password confirm must be match password";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginRequest());
        return "loginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginRequest request,
                              BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "loginView";
        }

        try {
            User user = authenticationHelper.tryAuthenticateUser(request);
            session.setAttribute("currentUser", user.getUsername());
            session.setAttribute("isAdmin", user.isAdmin());
            session.setAttribute("isBanned", user.isBanned());
            session.setAttribute("username",user.getUsername());
            if (session.getAttribute("isAdmin").equals(true)){
                return "redirect:/admin";
            }
            if (session.getAttribute("isBanned").equals(true)){
                return "redirect:/home";
            }

            return "redirect:/auth/page";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "loginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterRequest());
        return "registerView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterRequest request,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registerView";
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            bindingResult.rejectValue("password", "register_error", PASSWORD_CONFIRM_NOT_MATCH);
            return "registerView";
        }
        User user = userMapper.fromRegisterRequest(request);
        try {
            userService.create(user);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "registerView";
        }

    }

    @GetMapping("/page")
    public String userViewPage(Model model) {
        model.addAttribute("user", new UserRequest());
        return "userPageView";
    }
}

