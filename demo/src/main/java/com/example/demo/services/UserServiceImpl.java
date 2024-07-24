package com.example.demo.services;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public List<User> getUsers(User user) {
        return repository.getAllUsers();
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);

    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);

    }

    @Override
    public User create(User user) {
        boolean duplicate = true;

        try {
            repository.getByUsername(user.getUsername());
        }catch (EntityNotFoundException e){
            duplicate = false;
        }

        if (duplicate){
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        return repository.createUser(user);

    }
}
