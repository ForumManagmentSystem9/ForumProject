package com.example.demo.repositories;

import com.example.demo.models.User;

import java.util.List;

public interface UserRepository {
    void createUser(User user);

    List<User> getAllUsers();

    User getByEmail(String email);

    User getByUsername(String username);

    List<User> search(String keyword);
    User get(int id);
    void update(User user);
}
