package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAll(int id);
    Comment getById(int post_id, int comment_id);
    void create(int post_id, Comment comment, User user);
    void update(int post_id, Comment comment, User user);
    void delete(int post_id, int comment_id, User user);
}
