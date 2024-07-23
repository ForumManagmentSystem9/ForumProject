package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAll(Comment comment);
    Comment getById(int id);
    void create(Comment comment, User user);
    void update(Comment comment, User user);
    void delete(int id, User user);
    void like(int id, User user);
}
