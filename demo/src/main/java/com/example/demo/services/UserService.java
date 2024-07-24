package com.example.demo.services;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {
    List<User>getUsers(User user);
    User getById(int id);
    User getByUsername(String username);
    User create(User user);
}
