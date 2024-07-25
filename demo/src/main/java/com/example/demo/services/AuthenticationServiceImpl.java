package com.example.demo.services;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationServiceImpl(UserRepository repository, PasswordEncoder encoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public AuthenticationResponse registerUser(User request) {
        boolean duplicate = true;
        try {
            repository.getByEmail(request.getEmail());
        }
        catch (EntityNotFoundException e){
            duplicate = false;
        }
        if (duplicate){
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
        String token  = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }
//    private User fillUserData(User newUser){
//        User user = new User();
//        user.setUsername(newUser.getUsername());
//        user.setEmail(newUser.getEmail());
//        user.setPassword(encoder.encode(newUser.getPassword()));
//        user.setFirstName(newUser.getFirstName());
//        user.setLastName(newUser.getLastName());
//        user.setRole(newUser.getRole());
//
//        return user;
//    }
}
