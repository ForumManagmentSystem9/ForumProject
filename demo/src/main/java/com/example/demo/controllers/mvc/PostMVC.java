package com.example.demo.controllers.mvc;

import com.example.demo.helpers.DataHelper;
import com.example.demo.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PostMVC {
    private final PostService postService;
    private final DataHelper dataHelper;

    @Autowired
    public PostMVC(PostService postService, DataHelper dataHelper) {
        this.postService = postService;
        this.dataHelper = dataHelper;
    }

    @GetMapping("/post/{id}")
    public String loadPost(@PathVariable int id, Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");

        model.addAttribute("formattedDate", dataHelper.dataFormatter());

        var post = postService.getPostById(id);
        model.addAttribute("post", post);

        return "post";
    }
}
