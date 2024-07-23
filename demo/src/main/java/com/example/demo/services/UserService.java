package com.example.demo.services;

import com.example.demo.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    void registerUser(User user);
    User updateUser(User user);
    User getUserById(int id);
    UserDetails getUserByEmail(String email);
    List<User> getUserByKeyword(String keyword);
    void changePassword(int id, String newPassword);
    void blockUser(int id);
}
