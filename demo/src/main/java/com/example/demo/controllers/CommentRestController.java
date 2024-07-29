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
    public List<Comment> getAll(@RequestHeader HttpHeaders headers){
        try{
            User user = authorizationHelper.extractUserFromHeaders(headers);
            authorizationHelper.isUserAdmin(user);
            return service.getAll();
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Comment getCommentById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.extractUserFromHeaders(headers);
            return service.getById(user.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void createComment(@RequestHeader HttpHeaders headers, @RequestBody Comment comment) {
        try {
            User user = authorizationHelper.extractUserFromHeaders(headers);
            comment.setUser(user);
            service.create(comment, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public void likeComment(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authorizationHelper.extractUserFromHeaders(headers);
            service.like(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
