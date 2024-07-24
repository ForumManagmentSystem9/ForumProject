//package com.example.demo.controllers;
//
//
//import com.example.demo.exceptions.AuthorizationException;
//import com.example.demo.exceptions.EntityNotFoundException;
//import com.example.demo.helpers.AuthenticationHelper;
//import com.example.demo.models.Comment;
//import com.example.demo.models.Role;
//import com.example.demo.models.User;
//import com.example.demo.services.CommentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/comment")
//public class CommentRestControler {
//
//    public static final String ERROR_MESSAGE = "You are not admin, or author to do this operation";
//    private final CommentService service;
//    private final AuthenticationHelper authenticationHelper;
//
//    @Autowired
//    public CommentRestControler(CommentService service, AuthenticationHelper authenticationHelper) {
//        this.service = service;
//        this.authenticationHelper = authenticationHelper;
//    }
//
//    @GetMapping
//    public List<Comment> getAll(@RequestHeader HttpHeaders headers){
//        try{
//            User user = authenticationHelper.tryGetUser(headers);
//            if (!user.getRole().getName().equals(Role.RoleType.ADMIN)){
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
//                        ERROR_MESSAGE);
//
//            }
//            return service.getAll();
//        }catch (AuthorizationException e){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
//                    e.getMessage());
//        }
//    }
//    @GetMapping("/{id}")
//    public Comment getCommentById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            authenticationHelper.tryGetUser(headers);
//            return service.getById(id);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @PostMapping
//    public void createComment(@RequestHeader HttpHeaders headers, @RequestBody Comment comment) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            comment.setUser(user);
//            service.create(comment, user);
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @PostMapping("/{id}/like")
//    public void likeComment(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            service.like(id, user);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//
//}
