package com.example.demo.controlers.tests;

import com.example.demo.controllers.PrivateController;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrivateControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationHelper authorizationHelper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PrivateController privateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testGetUsers_Success() {
        User mockUser = new User();
        List<User> mockUserList = Collections.singletonList(mockUser);

        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(mockUser);
        when(userService.userList(mockUser)).thenReturn(mockUserList);

        ResponseEntity<List<User>> response = privateController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserList, response.getBody());
    }

    @Test
    void testGetUsers_Unauthorized() {
        when(authorizationHelper.extractUserFromToken(authentication)).thenThrow(new AuthorizationException("Unauthorized"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> privateController.getUsers());

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }

    @Test
    void testChangeBlockStatus_Success() {
        User mockUser = new User();

        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(mockUser);
        doNothing().when(userService).blockUser(anyInt());

        ResponseEntity<User> response = privateController.changeBlockStatus(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).blockUser(1);
    }

    @Test
    void testChangeBlockStatus_Unauthorized() {
        when(authorizationHelper.extractUserFromToken(authentication)).thenThrow(new AuthorizationException("Unauthorized"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> privateController.changeBlockStatus(1));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }

    @Test
    void testSetRoles_Success() {
        User mockUser = new User();
        User userToUpdate = new User();
        Role mockRole = new Role();

        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(mockUser);
        when(userService.getUserById(1)).thenReturn(userToUpdate);
        doNothing().when(userService).changeRole(mockUser, userToUpdate);

        ResponseEntity<User> response = privateController.setRoles(1, mockRole);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).changeRole(mockUser, userToUpdate);
    }

    @Test
    void testSetRoles_Unauthorized() {
        when(authorizationHelper.extractUserFromToken(authentication))
                .thenThrow(new AuthorizationException("Unauthorized"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> privateController.setRoles(1, new Role()));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }
}
