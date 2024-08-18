package com.example.demo.controllers.mvc;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.DataHelper;
import com.example.demo.helpers.PostMapper;
import com.example.demo.models.Post;
import com.example.demo.models.PostDTO;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMVC {
    private final UserService userService;
    private final PostService postService;
    private final DataHelper dataHelper;
    private final AuthorizationHelper authorizationHelper;
    private final PostMapper postMapper;

    @Autowired
    public PostMVC(UserService userService, PostService postService, DataHelper dataHelper, AuthorizationHelper authorizationHelper, PostMapper postMapper) {
        this.userService = userService;
        this.postService = postService;
        this.dataHelper = dataHelper;
        this.authorizationHelper = authorizationHelper;
        this.postMapper = postMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(Authentication authentication){
        return authentication != null  && authentication.isAuthenticated();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request){
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllPosts(Model model, Authentication authentication){
        try {
            if (!populateIsAuthenticated(authentication)){
                List<Post> top10NewestPosts = postService.getTop10NewestPosts();
                model.addAttribute("posts", top10NewestPosts);
                return "Top10PostView";
            }
            User user = authorizationHelper.extractUserFromToken(authentication);
            List<Post> allPosts = postService.getAllPosts();
            model.addAttribute("posts", allPosts);
            return "PostsView";
        }catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable int id, Model model,
                              Authentication authentication){
        try {
            if (!populateIsAuthenticated(authentication)){
                return "redirect:/auth/login";
            }
            Post post = postService.getPostById(id);
            model.addAttribute("post", post);
            return "PostView";
        }catch (EntityNotFoundException e){
            return "ErrorView";
        }catch (AuthorizationException e){
            return "refirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post")PostDTO postDTO,
                             BindingResult bindingResult, Authentication authentication){
        if (bindingResult.hasErrors()){
            return "PostCreateView";
        }
        try {
            User user = authorizationHelper.extractUserFromToken(authentication);
            Post post = postMapper.fromDto(postDTO, user);
            postService.createPost(post);
            return "redirect:/post";
        }catch (AuthorizationException e){
            return "redirect:/auth/login";
        }catch (EntityNotFoundException e){
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditPostPage(@PathVariable int id, Model model, Authentication authentication) {
        try {
            if (!populateIsAuthenticated(authentication)) {
                return "redirect:/auth/login";
            }

            Post post = postService.getPostById(id);
            model.addAttribute("post", post);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDTO postDto,
                             BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "PostCreateView";
        }

        try {
            User user = authorizationHelper.extractUserFromToken(authentication);
            Post post = postMapper.fromDto(id, postDto, user);
            postService.updatePost(post, user);
            return "redirect:/posts/" + id;
        } catch (AuthorizationException | EntityNotFoundException e) {
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Authentication authentication) {
        try {
            if (!populateIsAuthenticated(authentication)) {
                return "redirect:/auth/login";
            }

            User user = authorizationHelper.extractUserFromToken(authentication);
            postService.deletePostById(id, user);
            return "redirect:/posts";
        } catch (AuthorizationException | EntityNotFoundException e) {
            return "ErrorView";
        }
    }

}
