package com.example.demo.filter;

import com.example.demo.models.userfolder.CustomUserDetails;
import com.example.demo.models.userfolder.User;
import com.example.demo.services.JWTService;
import com.example.demo.services.UserService;
import com.example.demo.helpers.AuthenticationHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService service;
    private final UserService userService;

    @Autowired
    public JWTAuthenticationFilter(JWTService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.substring(7);
            logger.debug("Extracted token: " + token);
            String email = service.extractEmail(token);
            logger.debug("Extracted email: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = new CustomUserDetails(userService.getUserByEmail(email));

                if (service.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    AuthenticationHelper helper = new AuthenticationHelper(request);
                    authentication.setDetails(helper);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication set for user: " + email);
                }
                else {
                    logger.warn("JWT token is invalid or expired");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing authentication", e);
        } finally {
            filterChain.doFilter(request, response);
        }

    }
}