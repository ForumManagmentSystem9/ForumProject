package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.Post;

import java.util.List;


public interface CommentRepository {
    List<Comment> getAll(int postId);

    Comment getById(int post_id, int comment_id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(int post_id, int comment_id);

    void getLikes(int id);
}
