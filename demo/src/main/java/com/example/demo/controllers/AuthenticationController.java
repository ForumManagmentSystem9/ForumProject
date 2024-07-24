package com.example.demo.controllers;

import com.example.demo.helpers.UserMapper;
import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDto request
    ) {
        User user = userMapper.fromDto(request);
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

//    @PostMapping("/refresh_token")
//    public ResponseEntity refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) {
//        return authService.refreshToken(request, response);
//    }

}
