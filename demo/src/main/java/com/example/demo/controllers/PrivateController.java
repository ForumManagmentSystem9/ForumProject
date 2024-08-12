package com.example.demo.controllers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.User;
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
@RequestMapping("api/private")
public class PrivateController {
    private final UserService service;
    private final AuthorizationHelper helper;

    @Autowired
    public PrivateController(UserService service, AuthorizationHelper helper){
        this.service = service;
        this.helper = helper;
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = helper.extractUserFromToken(auth);
            return ResponseEntity.ok(service.userList(user));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> changeBlockStatus(@PathVariable int id){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = helper.extractUserFromToken(auth);
            service.blockUser(id);
            return ResponseEntity.ok().build();
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PutMapping("/role/{id}")
    public ResponseEntity<User> setRoles(@PathVariable int id, @RequestBody Role role){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = helper.extractUserFromToken(auth);

            User userToUpdate = service.getUserById(id);

            service.changeRole(user, userToUpdate, role);
            return ResponseEntity.ok().build();
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
