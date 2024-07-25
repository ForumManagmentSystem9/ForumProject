package com.example.demo.helpers;

import com.example.demo.models.userfolder.User;
import com.example.demo.models.userfolder.UserLoginDTO;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoginMapper {
    private UserService service;

    @Autowired
    public UserLoginMapper(UserService service){
        this.service = service;
    }


    public User fromDto(UserLoginDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }
}
