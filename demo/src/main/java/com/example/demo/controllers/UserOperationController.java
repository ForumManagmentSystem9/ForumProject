package com.example.demo.controllers;

import com.example.demo.models.userfolder.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserOperationController {
    @PostMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(UserDTO userDto){
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
