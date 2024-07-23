package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder){
        this.repository = repository;
        this.encoder = encoder;
    }
    @Override
    public void registerUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(repository.getByUsername(user.getUsername()));
        if (existingUser.isPresent()) {
            throw new EntityNotFoundException("User", "email", user.getEmail());
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.createUser(user);
        }
    }

    @Override
    public User updateUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(repository.getByUsername(user.getUsername()));
        return null;
    }

    @Override
    public UserDetails getUserByEmail(String email) throws EntityNotFoundException{
        User user = repository.getByEmail(email);
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public List<User> getUserByKeyword(String keyword) {
        return null;
    }

    @Override
    public void changePassword(int id, String newPassword) {

    }

    @Override
    public void blockUser(int id) {

    }
}
