package com.example.demo.service.test;

import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.JWTService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    private User mockUser;
    private CustomUserDetails mockCustomUserDetails;
    private final String mockToken = "mock.jwt.token";
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setEmail("test@example.com");

        mockCustomUserDetails = new CustomUserDetails(mockUser);

        claims = new DefaultClaims();
        claims.setSubject(mockUser.getEmail());
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
    }

    @Test
    void testExtractEmail() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            JwtParserBuilder mockParserBuilder = mock(JwtParserBuilder.class);
            JwtParser mockParser = mock(JwtParser.class);

            when(Jwts.parserBuilder()).thenReturn(mockParserBuilder);
            when(mockParserBuilder.setSigningKey(any(SecretKey.class))).thenReturn(mockParserBuilder);
            when(mockParserBuilder.build()).thenReturn(mockParser);
            String email = jwtService.extractEmail(mockToken);
            assertEquals("test@example.com", email);
        }
    }


    @Test
    void testIsValid_Success() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            assertTrue(jwtService.isValid(mockToken, mockCustomUserDetails));
        }
    }

    @Test
    void testIsValid_Failure_InvalidEmail() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            claims.setSubject("invalid@example.com");
            assertFalse(jwtService.isValid(mockToken, mockCustomUserDetails));
        }
    }

    @Test
    void testIsValid_Failure_TokenExpired() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            claims.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            assertFalse(jwtService.isValid(mockToken, mockCustomUserDetails));
        }
    }

    @Test
    void testIsTokenExpired_False() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            assertFalse(jwtService.isTokenExpired(mockToken));
        }
    }

    @Test
    void testIsTokenExpired_True() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            claims.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            assertTrue(jwtService.isTokenExpired(mockToken));
        }
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(mockUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testRefreshToken_Success() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(jwtService.extractAllClaims(mockToken)).thenReturn(claims);
            String refreshedToken = jwtService.refreshToken(mockToken);
            assertNotNull(refreshedToken);
            assertFalse(refreshedToken.isEmpty());
        }
    }

    @Test
    void testRefreshToken_ExpiredToken() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(jwtService.extractAllClaims(mockToken)).thenThrow(new ExpiredJwtException(null, claims, "Token expired"));
            String refreshedToken = jwtService.refreshToken(mockToken);
            assertNotNull(refreshedToken);
            assertFalse(refreshedToken.isEmpty());
        }
    }

    @Test
    void testExtractAllClaims_ThrowsException() {
        try (MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class)) {
            when(Jwts.parserBuilder().setSigningKey(any(SecretKey.class)).build().parseClaimsJws(mockToken)).
                    thenThrow(new JwtException("Invalid token"));
            assertThrows(RuntimeException.class, () -> jwtService.extractAllClaims(mockToken));
        }
    }
}

