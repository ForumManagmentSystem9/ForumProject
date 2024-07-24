package com.example.demo.repositories;

import com.example.demo.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostsRepository {
    Post save(Post post);
    Optional<Post> findById(int id);
    List<Post> findAll();
    void deleteById(int id);

    List<Post> findByUserId(int userId);

    List<Post> findByTitleContaining(String title);

    void update(Post post);
}