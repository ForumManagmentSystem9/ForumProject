package com.example.demo.service.test;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.RestrictHelper;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.services.JWTService;
import com.example.demo.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RestrictHelper helper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(repository.getByEmail(user.getEmail())).thenReturn(user);

        assertThrows(EntityDuplicateException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(repository.getByEmail(user.getEmail())).thenThrow(new EntityNotFoundException("User"));
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(repository.createUser(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = userService.registerUser(user);

        assertEquals("token", response.getToken());
        verify(repository).createUser(user);
    }

    @Test
    void testAuthenticate_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(authenticationManager.authenticate(any())).thenReturn(null); // Mocked
        when(repository.getByEmail(user.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationResponse response = userService.authenticate(user);

        assertEquals("token", response.getToken());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);

        when(repository.getById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(repository.getByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertEquals(user, result);
    }

    @Test
    void testChangePassword() {
        User user = new User();
        user.setId(1);
        user.setPassword("oldPassword");

        when(repository.getById(1)).thenReturn(user);
        when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.changePassword(1, "newPassword");

        verify(repository).update(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testBlockUser() {
        User user = new User();
        user.setId(1);
        user.setBlocked(false);

        when(repository.getById(1)).thenReturn(user);

        userService.blockUser(1);

        verify(repository).update(user);
        assertTrue(user.isBlocked());
    }


    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1);

        userService.deleteUser(user);

        verify(repository).delete(user);
    }
}

