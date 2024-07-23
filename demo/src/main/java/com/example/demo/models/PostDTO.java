package com.example.demo.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

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
    @Positive
    private int creator_id;
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

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
}
