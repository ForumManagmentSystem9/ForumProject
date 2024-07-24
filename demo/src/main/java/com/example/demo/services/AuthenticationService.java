package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse registerUser(User request);

    AuthenticationResponse authenticate(User request);
}
