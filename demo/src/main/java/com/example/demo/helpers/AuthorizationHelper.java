package com.example.demo.helpers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Creatable;
import com.example.demo.models.Post;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizationHelper {
    private final UserService service;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationHelper.class);

    public AuthorizationHelper(UserService service){
        this.service = service;
    }
    public User extractUserFromToken(Authentication authentication) throws AuthorizationException {
        if (authentication != null && authentication.getDetails() instanceof AuthenticationHelper) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getEmail();
            logger.debug("Extracted email: " + email);

            User user = (User) service.getUserByEmail(email);
            if (user == null) {
                throw new AuthorizationException("User not found");
            }
            logger.debug("User found: " + user);
            return user;
        }
        throw new AuthorizationException("Authentication details are missing or invalid");
    }
}
