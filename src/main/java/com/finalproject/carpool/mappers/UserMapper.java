package com.finalproject.carpool.mappers;

import com.finalproject.carpool.models.User;
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

    /*public User fromRegisterDto(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        return user;
    }*/

    /*public User fromUserUpdateDto(int id, UserUpdateDto dto) {
        User user = userService.getById(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());

        return user;
    }*/
}
