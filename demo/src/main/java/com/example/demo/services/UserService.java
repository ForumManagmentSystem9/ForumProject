package com.example.demo.services;

import com.example.demo.models.userfolder.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    List<User> userList();
    void updateUser(User user);
    UserDetails getUserById(int id);
    User getUserByEmail(String email);
//    List<UserDetails> getUserByKeyword(String keyword);
    void changePassword(int id, String newPassword);
    void blockUser(int id);
}
