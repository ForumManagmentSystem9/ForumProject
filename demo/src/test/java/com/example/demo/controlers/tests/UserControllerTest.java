package com.example.demo.controlers.tests;

import com.example.demo.controllers.UserController;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.UserMapper;
import com.example.demo.models.userfolder.User;
import com.example.demo.models.userfolder.UserDTO;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthorizationHelper authorizationHelper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testFindUserByInfo() {
        User mockUser = new User();
        List<User> mockUserList = List.of(new User());
        String keyword = "test";

        when(authorizationHelper.extractUserFromToken(any(Authentication.class))).thenReturn(mockUser);
        when(userService.getUserByKeyword(mockUser, keyword)).thenReturn(mockUserList);

        ResponseEntity<List<User>> response = userController.findUserByInfo(keyword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserList, response.getBody());
    }

    @Test
    public void testFindUserByInfoWhenUnauthorized() {
        String keyword = "test";

        when(authorizationHelper.extractUserFromToken(any(Authentication.class))).thenThrow(new AuthorizationException("Unauthorized"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.findUserByInfo(keyword);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        AuthenticationResponse authResponse = new AuthenticationResponse("token");

        when(userMapper.fromDto(userDTO)).thenReturn(user);
        when(userService.registerUser(user)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = userController.register(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void testLoginUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        AuthenticationResponse authResponse = new AuthenticationResponse("token");

        when(userMapper.fromDto(userDTO)).thenReturn(user);
        when(userService.authenticate(user)).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = userController.login(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        User changeUser = new User();

        when(authorizationHelper.extractUserFromToken(any(Authentication.class))).thenReturn(user);
        when(userMapper.fromDto(userDTO)).thenReturn(changeUser);

        ResponseEntity<Void> response = userController.updateUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUser(user, changeUser);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();

        when(authorizationHelper.extractUserFromToken(any(Authentication.class))).thenReturn(user);

        ResponseEntity<Void> response = userController.deleteUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(user);
    }
}