package com.example.demo.repositories;

import com.example.demo.models.userfolder.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    List<User> getAllUsers();

    User getByEmail(String email);

    User getByUsername(String username);

    List<User> search(String keyword);
    User getById(int id);
    void update(User user);
}
