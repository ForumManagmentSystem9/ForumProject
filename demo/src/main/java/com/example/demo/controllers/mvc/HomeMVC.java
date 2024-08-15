package com.example.demo.controllers.mvc;

import com.example.demo.helpers.DataHelper;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HomeMVC {
    private final PostService postService;
    private final UserService userService;
    private final DataHelper dataHelper;

    @Autowired
    public HomeMVC(PostService postService, UserService userService, DataHelper dataHelper) {
        this.postService = postService;
        this.userService = userService;
        this.dataHelper = dataHelper;
    }

    @GetMapping("/homepage")
    public String home(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");

        model.addAttribute("formattedDate", dataHelper.dataFormatter());

        if (email != null) {
            User user = userService.getUserByEmail(email);
            session.setAttribute("authenticated", true);
            handleAuthenticatedAccess(user, model);
        } else {
            session.setAttribute("authenticated", false);
            handleUnauthenticatedAccess(model);
        }

        return "home";
    }

    private void handleAuthenticatedAccess(User user, Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
    }

    private void handleUnauthenticatedAccess(Model model) {
        List<Post> top10MostCommented = postService.getTop10MostCommentedPosts();
        List<Post> top10Newest = postService.getTop10NewestPosts();

        model.addAttribute("postsComment", top10MostCommented);
        model.addAttribute("newPosts", top10Newest);
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // Refers to a Thymeleaf template named 'login.html'
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";  // Refers to a Thymeleaf template named 'signup.html'
    }
}
