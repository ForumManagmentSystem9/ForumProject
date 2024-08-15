package com.example.demo.controllers.rest;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.CommentMapper;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentDTO;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.CommentService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/posts/{postId}/comments")
public class CommentRestController {
    private final CommentService service;
    private final UserService userService;
    private final AuthorizationHelper authorizationHelper;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentRestController(CommentService service, UserService userService, AuthorizationHelper authorizationHelper, CommentMapper commentMapper) {
        this.service = service;
        this.userService = userService;
        this.authorizationHelper = authorizationHelper;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<Comment> getAll(@PathVariable int postId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            return service.getAll(postId);
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable int postId, @PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            return service.getById(postId, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void createComment(@PathVariable int postId, @RequestBody CommentDTO commentDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            Comment comment = commentMapper.fromDto(commentDTO, user);
            service.create(postId, comment, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateComment(@PathVariable int postId, @RequestBody CommentDTO commentDTO, @PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            Comment comment = commentMapper.fromDto(id, commentDTO, user);
            service.update(postId, comment, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable int postId, @PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(authentication);
            service.delete(postId, id, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
