package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post savePost(Post post);
    Optional<Post> getPostById(int id);
    List<Post> getAllPosts();

    void deletePostById(int id, User user);

    List<Post> getPostsByUserId(int userId);
    List<Post> getPostsByTitleContaining(String title);

    void updatePost(Post post, User user);

    List<Post> getTop10MostCommentedPosts();
    List<Post> getTop10NewestPosts();
}
