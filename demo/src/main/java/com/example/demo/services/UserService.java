package com.example.demo.services;

import com.example.demo.models.userfolder.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void updateUser(User user);
    UserDetails getUserById(int id);
    UserDetails getUserByEmail(String email);
//    List<UserDetails> getUserByKeyword(String keyword);
    void changePassword(int id, String newPassword);
    void blockUser(int id);
}
