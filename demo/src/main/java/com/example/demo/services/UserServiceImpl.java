package com.example.demo.services;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Role;
import com.example.demo.helpers.RestrictHelper;
import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.AuthenticationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RestrictHelper helper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder, JWTService jwtService, @Lazy AuthenticationManager authenticationManager, RestrictHelper helper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.helper = helper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticationResponse registerUser(User request) {
        boolean duplicate = true;
        try {
            repository.getByEmail(request.getEmail());
        } catch (EntityNotFoundException e) {
            duplicate = false;
        }
        if (duplicate) {
            throw new EntityDuplicateException("User", "email", request.getEmail());
        }
        request.setPassword(encoder.encode(request.getPassword()));
        repository.createUser(request);
        String token = jwtService.generateToken(request);

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        User user = repository.getByEmail(request.getEmail());
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    @Override
    public void updateUser(User user, User changeUser) {
        updateUserField(user, changeUser);
        repository.update(user);
    }


    @Override
    public User getUserById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public List<User> userList(User user) {
        return repository.getUsers();
    }
    @Override
    public List<User> getUserByKeyword(User user, String keyword) {
        return repository.search(keyword);
    }

    @Override
    public void changePassword(int id, String newPassword) {
        User user = repository.getById(id);
        user.setPassword(newPassword);
        repository.update(user);
    }

    @Override
    public void blockUser(int id) {
        User user = repository.getById(id);
        boolean check =  user.isBlocked();

        if (check){
            user.setBlocked(false);
        }
        else {
            user.setBlocked(true);
        }
        repository.update(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email);
        return new CustomUserDetails(user);
    }

    @Override
    @Transactional
    public void changeRole(User user, User changeUser, Role role){
        helper.isUserAdmin(user);
        changeUser.setRole(role);
        repository.update(changeUser);
    }

    @Override
    public void deleteUser(User user) {
        user.getComments().clear();
        user.getPosts().clear();
        repository.delete(user);
    }

    private void updateUserField(User user, User updateUser) {
        if (updateUser.getFirstName() != null && !updateUser.getFirstName().equals(user.getFirstName())){
            user.setFirstName(updateUser.getFirstName());
        }
        if (updateUser.getLastName() != null && !updateUser.getLastName().equals(user.getLastName())){
            user.setLastName(updateUser.getLastName());
        }
        if (updateUser.getPassword() != null && !updateUser.getPassword().equals(user.getPassword())){
            user.setPassword(updateUser.getPassword());
        }
        repository.update(user);
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

}
