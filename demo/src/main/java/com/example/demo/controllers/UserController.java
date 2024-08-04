package com.example.demo.controllers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.UserMapper;
import com.example.demo.models.userfolder.User;
import com.example.demo.models.userfolder.UserDTO;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;
    private final UserMapper userMapper;
    private final AuthorizationHelper helper;

    @Autowired
    public UserController(UserService service, UserMapper userMapper, AuthorizationHelper helper) {
        this.service = service;
        this.userMapper = userMapper;
        this.helper = helper;
    }
    @GetMapping
    public ResponseEntity<List<User>> findUserByInfo(@RequestParam String keyword){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = helper.extractUserFromToken(auth);
            return ResponseEntity.ok(service.getUserByKeyword(user, keyword));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO request) {
        User user = userMapper.fromDto(request);
        return ResponseEntity.ok(service.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserDTO request) {
        User user = userMapper.fromDto(request);
        return ResponseEntity.ok(service.authenticate(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = helper.extractUserFromToken(auth);
        User changeUser = userMapper.fromDto(request);
        service.updateUser(user, changeUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = helper.extractUserFromToken(auth);
        service.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}

