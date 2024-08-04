package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.RestrictHelper;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final String MODIFY_POST_ERROR_MESSAGE = "Only admin or post creator can modify a post.";

    private final PostsRepository postsRepository;
    private final RestrictHelper helper;

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository, RestrictHelper helper) {
        this.postsRepository = postsRepository;
        this.helper = helper;
    }

    @Override
    public Post createPost(Post post) {
        return postsRepository.create(post);
    }

    @Override
    public Post getPostById(int id) {
        return postsRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Post> getAllPosts() {
        return postsRepository.findAll();
    }


    @Override
    public void deletePostById(int id, User user) {
        Post post = postsRepository.findById(id).orElseThrow();
        helper.deletePermission(post, user);
        postsRepository.deleteById(id);
    }
    @Override
    public List<Post> getTop10MostCommentedPosts() {
        return postsRepository.findTop10ByOrderByCommentsCountDesc();
    }

    @Override
    public List<Post> getTop10NewestPosts() {
        return postsRepository.findTop10ByOrderByCreatedDateDesc();
    }

    @Override
    public List<Post> getPostsByUserId(int userId) {
        return postsRepository.findByUserId(userId);
    }

    @Override
    public List<Post> getPostsByTitleContaining(String title) {
        return postsRepository.findByTitleContaining(title);
    }


    @Override
    public void updatePost(Post post, User user) {
        helper.isUserACreator(post, user);


        Post existingPosts = postsRepository.findById(post.getId()).orElseThrow(
                () -> new EntityNotFoundException("Post", post.getId())
        );

        updatePostField(existingPosts, post);
        postsRepository.update(existingPosts);
    }

    @Override
    public void like(int id, User user) {
        // Fetch the post, or throw an EntityNotFoundException if not found
        Post post = postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));

        // Assuming addLike is a method in your repository to handle the like operation
        postsRepository.addLike(post, user);
    }

    private void updatePostField(Post post, Post changePost) {
        if (changePost.getTitle() != null && !changePost.getTitle().equals(post.getTitle())){
            post.setTitle(changePost.getTitle());
        }
        if (changePost.getContent() != null && !changePost.getContent().equals(post.getContent())){
            post.setContent(changePost.getContent());
        }
    }
}
