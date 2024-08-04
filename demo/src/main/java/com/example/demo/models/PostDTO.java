package com.example.demo.models;

import com.example.demo.models.userfolder.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PostDTO {
    @NotNull
    @Size(min = 16, max = 64,
            message = "The title should be between 16 and 64 symbols")
    private String title;
    @NotNull
    @Size(min = 32, max = 8192,
            message = "The content should be between 32 and 8192 symbols")
    private String content;
    private String likes;

    private User creator;
    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();
    public PostDTO(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
