package com.example.demo.helpers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizationHelper {
    public static final String ADMIN_ERROR_MESSAGE = "You are not admin";
    public static final String ADMIN_MODERATOR_ERROR_MESSAGE = "You must be admin or moderator";
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
    public boolean isUserAdmin(User user){
        if (!user.getRole().getName().equals(Role.RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ADMIN_ERROR_MESSAGE);
        }
        logger.debug("User role is not admin: " + user.getRole());
        return true;
    }
    public boolean isUserAdminOrModerator(User user){
        if (!user.getRole().getName().equals(Role.RoleType.ADMIN) && !user.getRole().getName().equals(Role.RoleType.MODERATOR)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ADMIN_ERROR_MESSAGE);
        }
        logger.debug("User role is not admin: " + user.getRole());
        return true;
    }
}
