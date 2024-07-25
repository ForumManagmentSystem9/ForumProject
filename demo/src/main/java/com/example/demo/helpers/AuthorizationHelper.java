package com.example.demo.helpers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizationHelper {
    public static final String ADMIN_ERROR_MESSAGE = "You are not admin";
    private final UserService service;

    public AuthorizationHelper(UserService service){
        this.service = service;
    }
    public User extractUserFromHeaders(HttpHeaders headers) throws AuthorizationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof AuthenticationHelper) {
            AuthenticationHelper authHelper = (AuthenticationHelper) authentication.getDetails();
            String email = authHelper.getCustomField();
            User user = (User) service.getUserByEmail(email);
            if (user == null) {
                throw new AuthorizationException("User not found");
            }
            return user;
        }
        throw new AuthorizationException("Authentication details are missing or invalid");
    }
    public boolean isUserAdmin(User user){
        if (!user.getRole().getName().equals(Role.RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ADMIN_ERROR_MESSAGE);
        }
        return true;
    }
}
