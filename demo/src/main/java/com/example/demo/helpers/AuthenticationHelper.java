package com.example.demo.helpers;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper{
    private static final String AUTHENTICATION_HEADER_NAME = "Authentication";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MODERATOR_ROLE = "MODERATOR";

    private final UserService service;

    @Autowired
    public AuthenticationHelper(UserService service) {
        this.service = service;
    }

    public User tryGetUser(HttpHeaders headers){
        if (!headers.containsKey(AUTHENTICATION_HEADER_NAME)){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHENTICATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = service.getByUsername(username);

            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        }catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

    }

    public void checkForAdminOrModerator(HttpHeaders headers){
        User user = tryGetUser(headers);
        if (!isAdmin(user) && !isModerator(user)){
            throw new AuthorizationException("You must be admin or moderator!");
        }
    }

    private boolean isAdmin(User user){
        return user.getRole().getName().equals(ADMIN_ROLE);
    }

    private boolean isModerator(User user){
        return user.getRole().getName().equals(MODERATOR_ROLE);
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == - 1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo){
        int firstSpace = userInfo.indexOf(" ");
        if(firstSpace == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace + 1);
    }
}
