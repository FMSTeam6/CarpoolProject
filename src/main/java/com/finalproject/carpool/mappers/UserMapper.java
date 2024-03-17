package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.User;
import com.finalproject.carpool.models.requests.user.RegisterRequest;
import com.finalproject.carpool.models.requests.user.UserRequest;
import com.finalproject.carpool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;
    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromRequest(int id, UserRequest request) {
        User user = fromRequest(request);
        user.setId(id);
        return user;
    }
    public User fromRequest(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        return user;
    }

    public User fromRegisterRequest(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        return user;
    }

//    public User fromUserUpdateDto(int id, UserUpdateRequest request) {
//        User user = userService.getById(id);
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setPassword(request.getPassword());
//
//        return user;
//    }
}
