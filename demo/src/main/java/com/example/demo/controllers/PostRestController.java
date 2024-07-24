package com.example.demo.controllers;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.PostService;
import com.example.demo.helpers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostRestController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );
    }

    @PostMapping
    public Post createPost(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDTO postDTO) {
        try {
            User user = getCurrentUser();
            Post post = postMapper.fromDto(postDTO);
            return postService.savePost(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody PostDTO postDTO) {
        try {
            User user = getCurrentUser();
            Post post = postMapper.fromDto(postDTO);
            postService.updatePost(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = getCurrentUser();
            postService.deletePostById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
