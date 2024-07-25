package com.example.demo.controllers;

import com.example.demo.helpers.UserLoginMapper;
import com.example.demo.helpers.UserMapper;
import com.example.demo.models.userfolder.User;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserMapper userMapper;
    private final UserLoginMapper userLoginMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authService, UserMapper userMapper, UserLoginMapper userLoginMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.userLoginMapper = userLoginMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {
//        User user = userMapper.fromDto(request);
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
//        User user = userLoginMapper.fromDto(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
