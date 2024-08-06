package com.example.demo.controllers;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import com.example.demo.response.PostsResponse;
import com.example.demo.services.PostService;
import com.example.demo.helpers.PostMapper;
import com.example.demo.helpers.AuthorizationHelper;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public PostRestController(PostService postService, PostMapper postMapper, AuthorizationHelper authorizationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @GetMapping
    public ResponseEntity<PostsResponse> getAllPosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            PostsResponse response = handleUnauthenticatedAccess();
            return ResponseEntity.ok(response);
        } else {
            User user = authorizationHelper.extractUserFromToken(auth);
            List<Post> allPosts = postService.getAllPosts();
            return ResponseEntity.ok(new PostsResponse(allPosts));
        }
    }

    private PostsResponse handleUnauthenticatedAccess() {
        List<Post> top10MostCommented = postService.getTop10MostCommentedPosts();
        List<Post> top10Newest = postService.getTop10NewestPosts();

        return new PostsResponse(top10MostCommented, top10Newest);
    }


    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public Post createPost(@Valid @RequestBody PostDTO postDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(auth);
            Post post = postMapper.fromDto(postDTO, user);
            return postService.createPost(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable int id, @RequestBody PostDTO postDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(auth);
            Post post = postMapper.fromDto(id, postDTO, user);
            postService.updatePost(post, user);
            return post;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(auth);
            postService.deletePostById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable @NotNull int postId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authorizationHelper.extractUserFromToken(auth);
            postService.like(postId, user);
            return ResponseEntity.ok("Post liked successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}
