package com.example.demo.repositories;

import com.example.demo.models.Posts;

import java.util.List;
import java.util.Optional;

public interface PostsRepository {
    Posts save(Posts post);
    Optional<Posts> findById(int id);
    List<Posts> findAll();
    void deleteById(int id);

    List<Posts> findByUserId(int userId);

    List<Posts> findByTitleContaining(String title);

    void update(Posts post);
}