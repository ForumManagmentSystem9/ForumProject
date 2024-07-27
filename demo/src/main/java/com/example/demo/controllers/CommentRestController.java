package com.example.demo.controllers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.Comment;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.CommentService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentRestController {
    private final CommentService service;
    private final UserService userService;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public CommentRestController(CommentService service, UserService userService, AuthorizationHelper authorizationHelper) {
        this.service = service;
        this.userService = userService;
        this.authorizationHelper = authorizationHelper;
    }

    @GetMapping
    public List<Comment> getAll(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            authorizationHelper.isUserAdmin(user);
            return service.getAll();
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            return service.getById(user.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void createComment(@RequestBody Comment comment) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            comment.setUser(user);
            service.create(comment, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public void likeComment(@PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            service.like(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
