package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.User;

import java.util.List;


public interface CommentRepository {
    List<Comment> getAll(Comment comment);

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(int id);

    void getLikes(int id);




}
