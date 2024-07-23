package com.example.demo.services;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Posts;
import com.example.demo.models.User;
import com.example.demo.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private static final String MODIFY_POST_ERROR_MESSAGE = "Only admin or post creator can modify a post.";

    private final PostsRepository postsRepository;

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    public Posts savePost(Posts post) {
        return postsRepository.save(post);
    }

    @Override
    public Optional<Posts> getPostById(int id) {
        return postsRepository.findById(id);
    }

    @Override
    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }


    @Override
    public void deletePostById(int id, User user) {
        checkModifyPermissions(id, user);
        postsRepository.deleteById(id);
    }

    @Override
    public List<Posts> getPostsByUserId(int userId) {
        return postsRepository.findByUserId(userId);
    }

    @Override
    public List<Posts> getPostsByTitleContaining(String title) {
        return postsRepository.findByTitleContaining(title);
    }


    @Override
    public void updatePost(Posts post, User user) {
        checkModifyPermissions(post.getId(), user);

        boolean duplicateExists = true;
        try {
            List<Posts> existingPosts = postsRepository.findByTitleContaining(post.getTitle());
            if (existingPosts.isEmpty() || (existingPosts.size() == 1 && existingPosts.get(0).getId() == post.getId())) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        postsRepository.update(post);
    }

    private void checkModifyPermissions(int postId, User user) {
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post", postId));
        if (!post.getUser().equals(user)) {
            throw new AuthorizationException(MODIFY_POST_ERROR_MESSAGE);
        }
    }
}