package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.RestrictHelper;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl  implements CommentService{

    private final CommentRepository repository;
    private final PostsRepository postRepository;
    private final RestrictHelper helper;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, PostsRepository postRepository, RestrictHelper helper) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.helper = helper;
    }

    @Override
    public List<Comment> getAll(int id) {
        Post post = postRepository.findById(id).orElseThrow();
        if (post == null) {
            throw new EntityNotFoundException("Post", id);
        }
        return repository.getAll(id);
    }

    @Override
    public Comment getById(int post_id, int comment_id) {
        return repository.getById(post_id, comment_id);
    }

    @Override
    public void create(int post_id, Comment comment, User user) {
        Post post = postRepository.findById(post_id).orElseThrow();
        comment.setPost(post);
        comment.setUser(user);
        repository.create(comment);
    }

    @Override
    public void update(int post_id, Comment comment, User user) {
        Comment existingComment = repository.getById(post_id, comment.getId());
        helper.isUserACreator(existingComment, user);

        updateCommentField(existingComment, comment);

        repository.update(existingComment);
    }

    @Override
    public void delete(int post_id, int comment_id, User user) {
        Comment commentToDelete = repository.getById(post_id, comment_id);
        helper.deletePermission(commentToDelete, user);

        repository.delete(post_id, commentToDelete.getId());
    }

    private void updateCommentField(Comment comment, Comment changeComment) {
        if (changeComment.getContent() != null && !changeComment.getContent().equals(comment.getContent())){
            comment.setContent(changeComment.getContent());
        }
    }
}
