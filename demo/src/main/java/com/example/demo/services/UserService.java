package com.example.demo.services;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {
    void registerUser(User user);
    User updateUser(User user);
    User getUserById(int id);
    List<User> getUserByKeyword(String keyword);
    void changePassword(int id, String newPassword);
    void blockUser(int id);
}
