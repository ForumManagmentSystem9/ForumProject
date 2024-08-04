package com.example.demo.repositories;

import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;

import java.util.List;
import java.util.Optional;

public interface PostsRepository {
    Post create(Post post);
    Optional<Post> findById(int id);
    List<Post> findAll();
    void deleteById(int id);

    List<Post> findByUserId(int userId);

    List<Post> findByTitleContaining(String title);

    void update(Post post);
    void addLike(Post post, User user);
    List<Post> findTop10ByOrderByCreatedDateDesc();
    List<Post> findTop10ByOrderByCommentsCountDesc();
}