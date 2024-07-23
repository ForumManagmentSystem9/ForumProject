package com.example.demo.services;

import com.example.demo.models.Posts;
import com.example.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Posts savePost(Posts post);
    Optional<Posts> getPostById(int id);
    List<Posts> getAllPosts();

    void deletePostById(int id, User user);

    List<Posts> getPostsByUserId(int userId);
    List<Posts> getPostsByTitleContaining(String title);

    void updatePost(Posts post, User user);
}
