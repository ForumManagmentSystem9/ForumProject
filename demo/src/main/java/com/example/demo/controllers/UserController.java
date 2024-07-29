package com.example.demo.controllers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService service;
    private final AuthorizationHelper helper;

    @Autowired
    public UserController(UserService service, AuthorizationHelper helper){
        this.service = service;
        this.helper = helper;
    }
    @GetMapping("/all")
    public List<User> getUsers(Authentication auth) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = helper.extractUserFromToken(authentication);
            helper.isUserAdminOrModerator(user);
            return service.userList();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
