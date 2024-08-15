package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.userfolder.User;
import com.example.demo.response.AuthenticationResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    AuthenticationResponse registerUser(User request);
    AuthenticationResponse authenticate(User request);
    List<User> userList(User user);
    void updateUser(User user, User changeUser);
    User getUserById(int id);
    User getUserByEmail(String email);

    List<User> getUserByKeyword(User user, String keyword);
    void changePassword(int id, String newPassword);
    void blockUser(int id);

    @Transactional
    void changeRole(User user, User changeUser, Role role);

    void deleteUser(User user);
    String encodePassword(String password);
}
