package com.example.demo.helpers;

import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private UserService service;

    @Autowired
    public UserMapper(UserService service){
        this.service = service;
    }

    public User fromDto(int id, UserDto userDto){
        User user = fromDto(userDto);
        user.setId(id);

        return user;
    }

    public User fromDto(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(user.getEmail());

        return user;
    }

}
