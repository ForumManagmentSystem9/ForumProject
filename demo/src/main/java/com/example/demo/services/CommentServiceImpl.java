package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl  implements CommentService{

    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Comment comment, User user) {
        comment.setUser(user);
        repository.create(comment);

    }

    @Override
    public void update(Comment comment, User user) {
        Comment existingCommnet = repository.getById(comment.getId());
        if (existingCommnet == null){
            throw new EntityNotFoundException("Comment", comment.getId());
        }
        if (!existingCommnet.getUser().equals(user)){
            throw new AuthorizationException("User not authorised");
        }

        existingCommnet.setContent(comment.getContent());
        repository.update(existingCommnet);

    }

    @Override
    public void delete(int id, User user) {
        Comment commentToDelete = repository.getById(id);
        if (commentToDelete == null){
            throw new EntityNotFoundException("Comment", id);
        }

        if (user.getRole().getName() == Role.RoleType.ADMIN){
            repository.delete(id);
        }else{
            throw new AuthorizationException("You are not authorized to delete the post");
        }

    }

    @Override
    public void like(int id, User user){
        Comment comment = repository.getById(id);
        if (comment == null){
            throw new EntityNotFoundException("Comment not found", id);
        }
        repository.getLikes(id);
    }
}
